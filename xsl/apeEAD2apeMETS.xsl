<?xml version="1.0" encoding="UTF-8"?>
<!-- Script for converting an EAD with a lot of dao's to an EAD and one or more METS -->
<!-- Manuall adaptions of the script is necessary to customize the output -->
<!-- Comments marked "Work todo" describes how the scripts needs to be expanded -->
<!-- More explanations is needed to be added and the whole script needs more testing with all different versions of apeEAD outputs -->
<!-- Work todo: Not all METS documents are created, and sometimes more than one ends up in the same EMTS-documetn though it should be in seperate ones. -->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:mets="http://www.loc.gov/METS/"
                xmlns:rts="http://www.archivesportaleurope.net/Portal/profiles/rights/"
                xmlns="urn:isbn:1-931666-22-9" xmlns:me="test" xmlns:xlink="http://www.w3.org/1999/xlink"
                xpath-default-namespace="urn:isbn:1-931666-22-9" version="2.0" exclude-result-prefixes="me">

    <xsl:strip-space elements="did" />
    <xsl:output name="my-xhtml-output" method="xml" encoding="UTF-8" indent="yes"/>

    <!-- Path to where the METS will be found -->
    <xsl:variable name="link_to_mets">http://www.link_to_mets.ape/mets</xsl:variable>
    <xsl:variable name="output_directory" select="'output/'" />

    <!-- Settings -->
    <xsl:variable name="settings">
        <!-- Values: mets_minimum/mest_maximum-->
        <mets_profil>mets_minimum</mets_profil>
        <split_ead>fonds</split_ead>
        <!-- Values: mods/ead_long/ead_medium/ead_short/ead_minimum-->
        <metadata>ead_short</metadata>
        <!-- Create the filename and path to the METS document -->
        <path_mets>
            <xsl:value-of select="concat($link_to_mets, substring-before(max(tokenize(document-uri(.), '/')), '.xml'), '/')" />
        </path_mets>
        <!--Institution information-->
        <institution>
            <!-- Rightsdeclaration following apeMETSRights -->
            <!-- These needs to be manually updated -->
            <!-- Work todo: Can this be made easier? -->
            <rts:RightsDeclarationMD
                    xmlns="http://www.archivesportaleurope.net/Portal/profiles/rights/"
                    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                    xsi:schemaLocation="http://www.archivesportaleurope.net/Portal/profiles/rights/ apeMETSRights.xsd"
                    RIGHTSDECID="RIGHTSDECID1" RIGHTSCATEGORY="COPYRIGHTED"
                    OTHERCATEGORYTYPE="FREE ACCESS">
                <rts:RightsDeclaration CONTEXT="None">Rights declaration</rts:RightsDeclaration>
                <rts:RightsHolder>
                    <rts:RightsHolderName>Rightsholders name</rts:RightsHolderName>
                </rts:RightsHolder>
                <rts:Context CONTEXTCLASS="GENERAL PUBLIC">
                    <rts:UserName USERTYPE="BOTH"/>
                    <rts:Permissions DISCOVER="true" DISPLAY="true" COPY="false" DUPLICATE="false" MODIFY="false" DELETE="true" PRINT="false"/>
                </rts:Context>
            </rts:RightsDeclarationMD>
        </institution>
    </xsl:variable>

    <!-- Common standard values -->
    <xsl:variable name="value_settings">
        <!-- Work todo: This list can be extended -->
        <type_anchor>Finding aid</type_anchor>
        <type_structure>Archival</type_structure>
        <mimetype>image/jpg</mimetype>
        <mets_name>Institution name</mets_name>
    </xsl:variable>

    <!-- Functions -->
    <xsl:function name="me:is_element_c">
        <xsl:param name="element_name" />
        <xsl:value-of select="if(local-name($element_name)='c') then true() else false()" />
    </xsl:function>

    <!-- Create label for logical structmap -->
    <xsl:function name="me:get_label">
        <!-- Work todo: Which values should be use? -->
        <xsl:param name="context"/>
        <xsl:value-of select="if (normalize-space($context/did/unittitle)!='') then concat($context/did/unittitle,' (',$context/@level,')') else concat($context/did/unitid,' (Picture)')" />
    </xsl:function>

    <!-- Create the href -->
    <xsl:function name="me:get_href">
        <xsl:param name="context"/>
        <xsl:value-of select="$context/@xlink:href" />
    </xsl:function>

    <!-- Check the role attribute -->
    <xsl:function name="me:get_role">
        <!-- Work todo: Check that all versions of apeEAD are working -->
        <!-- Work todo: and also get the different use of other dao attributes into this! -->
        <!-- Work todo: make sure it handles the @USE in METS correctly -->
        <xsl:param name="context"/>
        <xsl:value-of select="$context/@xlink:role" />
    </xsl:function>

    <!-- Check the title attribute -->
    <xsl:function name="me:get_title">
        <xsl:param name="context"/>
        <xsl:value-of select="$context/@xlink:title" />
    </xsl:function>

    <!-- Check the daogrp and daoloc  -->
    <xsl:function name="me:is_correct_dao">
        <xsl:param name="dao_check"/>
        <xsl:value-of select="if(local-name($dao_check)='dao') then if(normalize-space($dao_check/me:get_href(.))!='') then true() else false() else false()" />
    </xsl:function>

    <!-- If no ID attribute exist in the c an internal one is created -->
    <xsl:function name="me:get_id">
        <xsl:param name="c_element"/>
        <xsl:value-of select="if(normalize-space($c_element/@id)!='') then $c_element/@id else generate-id($c_element)" />
    </xsl:function>

    <!--Continue working with the EAD-->
    <xsl:template match="/">
        <create_files>
            <xsl:for-each select="ead/child::node()">
                <xsl:choose>
                    <xsl:when test="upper-case($settings/mets_profil)='METS_MINIMUM'">
                        <xsl:apply-templates select="node()|@*" mode="create_mets_simple"/>
                    </xsl:when>
                    <xsl:when test="upper-case($settings/mets_profil)='METS_MAXIMUM'">
                        <xsl:apply-templates select="node()|@*" mode="create_mets_full"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:message>Wrong value use: mets_minimum/mets_maximum</xsl:message>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:for-each>
            <xsl:call-template name="create_new_ead"/>
        </create_files>
    </xsl:template>

    <!-- Create METS anchors -->
    <!-- todo: Those 3 templates are weird, to test and maybe delete -->
    <xsl:template match="node()" mode="create_mets_full">
        <xsl:apply-templates mode="create_mets_full">
            <xsl:with-param name="metadata_collector">
                <xsl:element name="{if(local-name(.)='archdesc') then 'archdesc' else 'c'}">
                    <xsl:copy-of select="@*[local-name(.)!='c_pos' and local-name(.)!='c_dao_pos' and local-name(.)!='c_meta']"/>
                    <xsl:copy-of select="child::node()[me:is_element_c(.)=false()]"/>
                </xsl:element>
            </xsl:with-param>
        </xsl:apply-templates>
    </xsl:template>
    <xsl:template match="node()" mode="create_mets_full">
        <xsl:param name="metadata_collector"/>
        <xsl:apply-templates mode="create_mets_full">
            <xsl:with-param name="metadata_collector">
                <xsl:copy-of select="$metadata_collector"/>
                <xsl:element name="{if(local-name(.)='archdesc') then 'archdesc' else 'c'}">
                    <xsl:copy-of select="@*[local-name(.)!='c_pos' and local-name(.)!='c_dao_pos' and local-name(.)!='c_meta']"/>
                    <xsl:copy-of select="child::node()[me:is_element_c(.)=false()]"/>
                </xsl:element>
            </xsl:with-param>
        </xsl:apply-templates>
    </xsl:template>
    <xsl:template match="node()" mode="create_mets_full">
        <xsl:param name="metadata_collector"/>
        <xsl:for-each select=".">
            <xsl:result-document href="{concat(me:get_id(.),'.xml')}" format="my-xhtml-output">
                <mets:mets xmlns:mets="http://www.loc.gov/METS/" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://www.loc.gov/METS/ apeMETS.xsd urn:isbn:1-931666-22-9 http://www.loc.gov/ead/ead.xsd" PROFILE="apeMETS">
                    <xsl:call-template name="metsHdr"/>
                    <!-- Work todo: should there be a dmdSec??? not in the profile though!!!! -->
                    <!--<xsl:for-each
                        select="descendant-or-self::node()[me:all_c2(.)=true()][child::node() or local-name(.)='dao'][me:only_dao(.)=true()]">
                        <xsl:choose>
                            <xsl:when test="upper-case($settings/metadata)='MODS'">
                                <!-\- Work todo: Should it be a call to a dmdSec? not in apeMETS but can be locally -\->
                                <!-\-xsl:call-template name="dmdSec_???"/-\->
                            </xsl:when>
                            <xsl:when test="contains(upper-case($settings/metadata),'EAD')">
                                <!-\- Work todo: Should this be done: not present in the profile though! -\->
                                <xsl:call-template name="dmdSec_ead">
                                    <xsl:with-param name="metadata_collector"
                                        select="$metadata_collector"/>
                                    <!-\- Work todo: check so its working properly -\->
                                    <!-\-<xsl:with-param name="identifier" select="me:get_id(.)"/>-\->
                                    <xsl:with-param name="position" select="position()"/>
                                </xsl:call-template>
                            </xsl:when>

                            <xsl:otherwise>
                                <!-\- Work todo: Check this and get correct values -\->
                                <xsl:message>Values wrong use:
                                    Values: mods/ead_long/ead_middle/ead_short</xsl:message>
                            </xsl:otherwise>
                        </xsl:choose>
                    </xsl:for-each>-->

                    <!--call other METS parts-->
                    <!-- Work todo: should we have a logical structmap??? -->
                    <xsl:call-template name="amdSec"/>
                    <xsl:call-template name="fileSec"/>
                    <xsl:call-template name="structMap_phys"/>
                </mets:mets>
            </xsl:result-document>
        </xsl:for-each>
    </xsl:template>

    <!-- Create logical structmap anchors -->
    <xsl:template name="structMap_log_anchor">
        <!-- Work todo: Check the function, it does not seem to be ok, it checks if "." is an element c and an element dao -->
        <mets:structMap TYPE="LOGICAL">
            <mets:div ID="LOG_1" ADMID="AMD1" DMDID="DMDLOG_1" LABEL="{me:get_label(.)}">
                <mets:mptr LOCTYPE="URL" xlink:href="{concat($settings/path_mets, if(name(parent::node()) != 'archdesc') then parent::node()/@id else concat('archdesc_', //c/daogrp[normalize-space(daoloc/@xlink:href) != '']//eadid, '_', generate-id(parent::node())),'.xml')}" />
                <xsl:for-each select="node()[me:is_element_c(.)=true()][me:is_correct_dao(.)=true()]">
                    <mets:div ID="{concat('LOG_',position()+1)}" DMDID="{concat('DMDLOG_',position()+1)}" TYPE="{$value_settings/type_anchor}" LABEL="{me:get_label(.)}">
                        <mets:mptr LOCTYPE="URL" xlink:href="{concat($settings/path_mets,@id,'.xml')}"/>
                    </mets:div>
                </xsl:for-each>
            </mets:div>
        </mets:structMap>
    </xsl:template>


    <xsl:template match="c[did/dao]" mode="create_mets_simple">
        <xsl:for-each select=".">
            <xsl:result-document href="{concat($output_directory, me:get_id(.), '.xml')}" format="my-xhtml-output">
                <mets:mets xmlns:mets="http://www.loc.gov/METS/" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://www.loc.gov/METS/ apeMETS.xsd urn:isbn:1-931666-22-9 http://www.loc.gov/ead/ead.xsd" PROFILE="apeMETS">
                    <xsl:call-template name="metsHdr" />
                    <!--get the correct dmdSec-->
                    <!-- Work todo: Get it to work if it should be here check values!!! -->
                    <!--<xsl:for-each select="self::node()[me:all_c2(.)=true()][me:only_dao(.)=true()]">
                        <xsl:choose>
                            <!-\- Work todo: Should we be able to add MODS??? -\->
                            <xsl:when test="upper-case($settings/metadata)='MODS'">
                                <xsl:call-template name="dmdSec_mods"/>
                            </xsl:when>
                            <xsl:when test="contains(upper-case($settings/metadata),'EAD')">
                                <!-\- Work todo: should EAD be entered? Check and get to work-\->
                                <xsl:call-template name="dmdSec_ead">
                                                                        <!-\-<xsl:with-param name="metadata_collector" select="$metadata_collector"/>
                                                                        <xsl:with-param name="identifier" select="me:get_id(.)"/>-\->
                                    <xsl:with-param name="position" select="position()"/>
                                </xsl:call-template>
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:message>Values not correct use
                                    Values: mods/ead_long/ead_middle/short</xsl:message>
                            </xsl:otherwise>
                        </xsl:choose>
                    </xsl:for-each>-->
                    <!-- Create the rest of the METS -->
                    <xsl:call-template name="amdSec"/>
                    <xsl:call-template name="fileSec"/>
                    <xsl:call-template name="structMap_phys"/>
                    <!-- Work todo: Should we have alogical structmap??? -->
                    <!--<mets:structMap TYPE="LOGICAL">
                        <mets:div>
                            <mets:div ID="{concat('LOG_','1')}" ADMID="AMD1" DMDID="{concat('DMDLOG_','1')}" TYPE="map" />
                        </mets:div>
                    </mets:structMap>-->
                    <!-- Work todo: o structlink in profile should we have it here? -->
                    <!--<mets:structLink>
                        <mets:smLink xlink:from="{concat('LOG_','1')}" xlink:to="{concat('PHYS_','1')}" />
                    </mets:structLink>-->
                </mets:mets>
            </xsl:result-document>
            <xsl:apply-templates mode="create_mets_simple"/>
        </xsl:for-each>
    </xsl:template>

    <xsl:template name="metsHdr">
        <mets:metsHdr CREATEDATE="{current-dateTime()}" LASTMODDATE="{current-dateTime()}">
            <mets:agent ROLE="CUSTODIAN">
                <mets:name>
                    <xsl:choose>
                        <xsl:when test="normalize-space(/ead/eadheader/filedesc/publicationstmt/publisher/text()) != ''">
                            <xsl:value-of select="normalize-space(/ead/eadheader/filedesc/publicationstmt/publisher/text())"/>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:value-of select="$value_settings/mets_name" />
                        </xsl:otherwise>
                    </xsl:choose>
                </mets:name>
            </mets:agent>
            <mets:altRecordID TYPE="apeID">
                <xsl:value-of select="me:get_id(.)" />
            </mets:altRecordID>
            <mets:metsDocumentID>
                <xsl:value-of select="concat(me:get_id(.), '.xml')" />
            </mets:metsDocumentID>
        </mets:metsHdr>
    </xsl:template>

    <!--administrative Metadata-->
    <xsl:template name="amdSec">
        <mets:amdSec ID="AMD1">
            <mets:rightsMD ID="rights1">
                <mets:mdWrap MDTYPE="OTHER" OTHERMDTYPE="apeMETSRights">
                    <mets:xmlData>
                        <xsl:copy-of select="$settings/institution/child::node()"/>
                    </mets:xmlData>
                </mets:mdWrap>
            </mets:rightsMD>
        </mets:amdSec>
    </xsl:template>

    <!--file_sec-->
    <!-- Build filegrp's -->
    <!-- Work todo: Get this to work, problems with the different filegrp that needs to be created from the information in the dao -->
    <xsl:template name="fileSec">
        <mets:fileSec>
            <xsl:if test="did/dao[@xlink:role='VIDEO']">
                <mets:fileGrp USE="VIDEO">
                    <xsl:for-each select="did/dao[@xlink:role='VIDEO']">
                        <xsl:variable name="link_role" select="me:get_role(.)"/>
                        <mets:file ID="{concat('FILE_', position(), '_', dao[me:get_role(.)=$link_role]/me:get_role(.))}" SIZE="0" MIMETYPE="{$value_settings/mimetype}">
                            <mets:FLocat LOCTYPE="URL" xlink:type="simple" xlink:href="{concat(dao[me:get_role(.)=$link_role]/me:get_href(.), self::node()[me:get_role(.)=$link_role]/me:get_href(.))}" />
                        </mets:file>
                    </xsl:for-each>
                </mets:fileGrp>
            </xsl:if>
            <xsl:if test="did/dao[@xlink:role='IMAGE']">
                <mets:fileGrp USE="DISPLAY">
                    <xsl:for-each select="did/dao[@xlink:role='IMAGE']">
                        <xsl:variable name="link_role" select="me:get_role(.)"/>
                        <mets:file ID="{concat('FILE_', position(), '_', dao[me:get_role(.)=$link_role]/me:get_role(.))}" SIZE="0" MIMETYPE="{$value_settings/mimetype}">
                            <mets:FLocat LOCTYPE="URL" xlink:type="simple" xlink:href="{concat(dao[me:get_role(.)=$link_role]/me:get_href(.), self::node()[me:get_role(.)=$link_role]/me:get_href(.))}" />
                        </mets:file>
                    </xsl:for-each>
                </mets:fileGrp>
            </xsl:if>
            <xsl:if test="did/dao[@xlink:role='3D']">
                <mets:fileGrp USE="3D">
                    <xsl:for-each select="did/dao[@xlink:role='3D']">
                        <xsl:variable name="link_role" select="me:get_role(.)"/>
                        <mets:file ID="{concat('FILE_', position(), '_', dao[me:get_role(.)=$link_role]/me:get_role(.))}" SIZE="0" MIMETYPE="{$value_settings/mimetype}">
                            <mets:FLocat LOCTYPE="URL" xlink:type="simple" xlink:href="{concat(dao[me:get_role(.)=$link_role]/me:get_href(.), self::node()[me:get_role(.)=$link_role]/me:get_href(.))}" />
                        </mets:file>
                    </xsl:for-each>
                </mets:fileGrp>
            </xsl:if>
            <xsl:if test="did/dao[@xlink:role='SOUND']">
                <mets:fileGrp USE="SOUND">
                    <xsl:for-each select="did/dao[@xlink:role='SOUND']">
                        <xsl:variable name="link_role" select="me:get_role(.)"/>
                        <mets:file ID="{concat('FILE_', position(), '_', dao[me:get_role(.)=$link_role]/me:get_role(.))}" SIZE="0" MIMETYPE="{$value_settings/mimetype}">
                            <mets:FLocat LOCTYPE="URL" xlink:type="simple" xlink:href="{concat(dao[me:get_role(.)=$link_role]/me:get_href(.), self::node()[me:get_role(.)=$link_role]/me:get_href(.))}" />
                        </mets:file>
                    </xsl:for-each>
                </mets:fileGrp>
            </xsl:if>
            <xsl:if test="did/dao[@xlink:role='TEXT']">
                <mets:fileGrp USE="TEXT">
                    <xsl:for-each select="did/dao[@xlink:role='TEXT']">
                        <xsl:variable name="link_role" select="me:get_role(.)"/>
                        <mets:file ID="{concat('FILE_', position(), '_', dao[me:get_role(.)=$link_role]/me:get_role(.))}" SIZE="0" MIMETYPE="{$value_settings/mimetype}">
                            <mets:FLocat LOCTYPE="URL" xlink:type="simple" xlink:href="{concat(dao[me:get_role(.)=$link_role]/me:get_href(.), self::node()[me:get_role(.)=$link_role]/me:get_href(.))}" />
                        </mets:file>
                    </xsl:for-each>
                </mets:fileGrp>
            </xsl:if>
            <xsl:if test="did/dao[(@xlink:role='UNSPECIFIED' or not(@xlink:role)) and not(@xlink:title='thumbnail')]">
                <mets:fileGrp USE="DEFAULT">
                    <xsl:for-each select="did/dao[(@xlink:role='UNSPECIFIED' or not(@xlink:role)) and not(@xlink:title='thumbnail')]">
                        <xsl:variable name="link_role" select="me:get_role(.)"/>
                        <mets:file ID="{concat('FILE_', position(), '_UNSPECIFIED')}" SIZE="0" MIMETYPE="{$value_settings/mimetype}">
                            <mets:FLocat LOCTYPE="URL" xlink:type="simple" xlink:href="{concat(dao[me:get_role(.)=$link_role]/me:get_href(.), self::node()[me:get_role(.)=$link_role]/me:get_href(.))}" />
                        </mets:file>
                    </xsl:for-each>
                </mets:fileGrp>
            </xsl:if>
            <xsl:if test="did/dao[@xlink:title='thumbnail']">
                <mets:fileGrp USE="THUMBS">
                    <xsl:for-each select="did/dao[@xlink:title='thumbnail']">
                        <xsl:variable name="link_role" select="me:get_role(.)"/>
                        <mets:file ID="{concat('FILE_', position(), '_THUMB')}" SIZE="0" MIMETYPE="{$value_settings/mimetype}">
                            <mets:FLocat LOCTYPE="URL" xlink:type="simple" xlink:href="{concat(dao[me:get_role(.)=$link_role]/me:get_href(.), self::node()[me:get_role(.)=$link_role]/me:get_href(.))}" />
                        </mets:file>
                    </xsl:for-each>
                </mets:fileGrp>
            </xsl:if>
        </mets:fileSec>
    </xsl:template>

    <!--structmap-->
    <!--create the logical map-->
    <!-- Work todo: Get the logical to work if we should have it?! -->
    <xsl:template name="structMap_log">
        <xsl:param name="identifier"/>
        <mets:structMap TYPE="LOGICAL">
            <mets:div TYPE="{$value_settings/type_structure}" LABEL="{concat(parent::node()/did/unittitle, ' (', parent::node()/@level, ')')}">
                <xsl:if test="normalize-space($settings/max_level)!=normalize-space($settings/split_ead)">
                    <mets:mptr LOCTYPE="URL" xlink:href="{concat($settings/path_mets, parent::node()/me:get_id(.), '.xml')}" />
                </xsl:if>
                <mets:div ID="{concat('LOG_', '1')}" ADMID="AMD1" DMDID="{concat('DMDLOG_', '1')}" LABEL="{me:get_label(.)}" TYPE="{$value_settings/type_structure}">
                    <xsl:variable name="count_start" select="@c_pos"/>
                    <xsl:variable name="count_meta_start" select="@c_meta"/>
                    <xsl:call-template name="structMap_log_rekursiv">
                        <xsl:with-param name="count_start" select="$count_start"/>
                        <xsl:with-param name="count_meta_start" select="$count_meta_start"/>
                        <xsl:with-param name="count_meta_end" select="$count_meta_start"/>
                    </xsl:call-template>
                </mets:div>
            </mets:div>
        </mets:structMap>
    </xsl:template>

    <xsl:template name="structMap_log_rekursiv">
        <xsl:param name="count_start"/>
        <xsl:param name="count_meta_start"/>
        <xsl:param name="count_meta_end"/>
        <!-- Work todo: get this to work if we should have it -->
        <xsl:for-each select="node()[me:is_element_c(.)=true()][me:is_correct_dao(.)=true()]">
            <xsl:variable name="count_end" select="@c_pos"/>
            <xsl:variable name="count_meta_end" select="if(@c_meta) then @c_meta else $count_meta_end"/>
            <mets:div ID="{concat('LOG_',$count_end - $count_start +1)}" DMDID="{concat('DMDLOG_',$count_meta_end - $count_meta_start +1)}" TYPE="{$value_settings/type_structure}" LABEL="{me:get_label(.)}">
                <xsl:if test="child::node()[me:is_element_c(.)=true()][me:is_correct_dao(.)=true()]">
                    <xsl:call-template name="structMap_log_rekursiv">
                        <xsl:with-param name="count_start" select="$count_start"/>
                        <xsl:with-param name="count_meta_start" select="$count_meta_start"/>
                        <xsl:with-param name="count_meta_end" select="$count_meta_end"/>
                    </xsl:call-template>
                </xsl:if>
                <!-- Slower version: <xsl:variable name="count1" select="count(preceding::node()[me:all_c(.)=true()][me:only_dao(.)=true()][ancestor-or-self::node()[me:get_id(.)=$identifier]])
                + count(ancestor::node()[ancestor::node()[me:get_id(.)=$identifier]][me:all_c(.)=true()]) + 2"/>-->
            </mets:div>
        </xsl:for-each>
    </xsl:template>

    <!--Build the physical map-->
    <xsl:template name="structMap_phys">
        <mets:structMap TYPE="PHYSICAL">
            <mets:div ID="PHYS_1">

                <xsl:variable name="thumbnail" select='did/dao[@xlink:title="thumbnail"]' />
                <xsl:variable name="numberOfThumbnails" select='count($thumbnail)' />
                <xsl:for-each select="did/dao[@xlink:title != 'thumbnail' or not(@xlink:title)][@xlink:role='VIDEO']">
                    <xsl:variable name="linkPosition" select="position()" />
                    <xsl:variable name="title">
                        <xsl:choose>
                            <xsl:when test="normalize-space(@xlink:title) != ''"><xsl:value-of select="./@xlink:title" /></xsl:when>
                            <xsl:otherwise><xsl:value-of select="../unittitle[1]/text()" /></xsl:otherwise>
                        </xsl:choose>
                    </xsl:variable>
                    <!-- if thumbnail exists -->
                    <xsl:variable name="thumbnailHref">
                        <xsl:choose>
                            <xsl:when test="$thumbnail">
                                <xsl:choose>
                                    <xsl:when test="$numberOfThumbnails >= $linkPosition">
                                        <xsl:value-of select="$thumbnail[$linkPosition]/@xlink:href" />
                                    </xsl:when>
                                    <xsl:otherwise>
                                        <xsl:value-of select="$thumbnail[1]/@xlink:href" />
                                    </xsl:otherwise>
                                </xsl:choose>
                            </xsl:when>
                        </xsl:choose>
                    </xsl:variable>
                    <xsl:variable name="positionInList" select="position()"/>
                    <xsl:variable name="positionInListThumbnail" select="count(preceding-sibling::dao[@xlink:title='thumbnail']) + 1"/>
                    <mets:div ID="{concat('PHYS_', $positionInListThumbnail + 1)}" ORDER="{$positionInListThumbnail}" LABEL="{$title}">
                        <mets:fptr FILEID="{concat('FILE_', $positionInList, '_', me:get_role(.))}"/>
                        <xsl:if test="$thumbnailHref">
                            <mets:fptr FILEID="{concat('FILE_', $positionInListThumbnail, '_THUMB')}"/>
                        </xsl:if>
                    </mets:div>
                </xsl:for-each>
                <xsl:for-each select="did/dao[@xlink:title != 'thumbnail' or not(@xlink:title)][@xlink:role='IMAGE']">
                    <xsl:variable name="linkPosition" select="position()" />
                    <xsl:variable name="title">
                        <xsl:choose>
                            <xsl:when test="normalize-space(@xlink:title) != ''"><xsl:value-of select="./@xlink:title" /></xsl:when>
                            <xsl:otherwise><xsl:value-of select="../unittitle[1]/text()" /></xsl:otherwise>
                        </xsl:choose>
                    </xsl:variable>
                    <!-- if thumbnail exists -->
                    <xsl:variable name="thumbnailHref">
                        <xsl:choose>
                            <xsl:when test="$thumbnail">
                                <xsl:choose>
                                    <xsl:when test="$numberOfThumbnails >= $linkPosition">
                                        <xsl:value-of select="$thumbnail[$linkPosition]/@xlink:href" />
                                    </xsl:when>
                                    <xsl:otherwise>
                                        <xsl:value-of select="$thumbnail[1]/@xlink:href" />
                                    </xsl:otherwise>
                                </xsl:choose>
                            </xsl:when>
                        </xsl:choose>
                    </xsl:variable>
                    <xsl:variable name="positionInList" select="position()"/>
                    <xsl:variable name="positionInListThumbnail" select="count(preceding-sibling::dao[@xlink:title='thumbnail']) + 1"/>
                    <mets:div ID="{concat('PHYS_', $positionInListThumbnail + 1)}" ORDER="{$positionInListThumbnail}" LABEL="{$title}">
                        <mets:fptr FILEID="{concat('FILE_', $positionInList, '_', me:get_role(.))}"/>
                        <xsl:if test="$thumbnailHref">
                            <mets:fptr FILEID="{concat('FILE_', $positionInListThumbnail, '_THUMB')}"/>
                        </xsl:if>
                    </mets:div>
                </xsl:for-each>
                <xsl:for-each select="did/dao[@xlink:title != 'thumbnail' or not(@xlink:title)][@xlink:role='3D']">
                    <xsl:variable name="linkPosition" select="position()" />
                    <xsl:variable name="title">
                        <xsl:choose>
                            <xsl:when test="normalize-space(@xlink:title) != ''"><xsl:value-of select="./@xlink:title" /></xsl:when>
                            <xsl:otherwise><xsl:value-of select="../unittitle[1]/text()" /></xsl:otherwise>
                        </xsl:choose>
                    </xsl:variable>
                    <!-- if thumbnail exists -->
                    <xsl:variable name="thumbnailHref">
                        <xsl:choose>
                            <xsl:when test="$thumbnail">
                                <xsl:choose>
                                    <xsl:when test="$numberOfThumbnails >= $linkPosition">
                                        <xsl:value-of select="$thumbnail[$linkPosition]/@xlink:href" />
                                    </xsl:when>
                                    <xsl:otherwise>
                                        <xsl:value-of select="$thumbnail[1]/@xlink:href" />
                                    </xsl:otherwise>
                                </xsl:choose>
                            </xsl:when>
                        </xsl:choose>
                    </xsl:variable>
                    <xsl:variable name="positionInList" select="position()"/>
                    <xsl:variable name="positionInListThumbnail" select="count(preceding-sibling::dao[@xlink:title='thumbnail']) + 1"/>
                    <mets:div ID="{concat('PHYS_', $positionInListThumbnail + 1)}" ORDER="{$positionInListThumbnail}" LABEL="{$title}">
                        <mets:fptr FILEID="{concat('FILE_', $positionInList, '_', me:get_role(.))}"/>
                        <xsl:if test="$thumbnailHref">
                            <mets:fptr FILEID="{concat('FILE_', $positionInListThumbnail, '_THUMB')}"/>
                        </xsl:if>
                    </mets:div>
                </xsl:for-each>
                <xsl:for-each select="did/dao[@xlink:title != 'thumbnail' or not(@xlink:title)][@xlink:role='SOUND']">
                    <xsl:variable name="linkPosition" select="position()" />
                    <xsl:variable name="title">
                        <xsl:choose>
                            <xsl:when test="normalize-space(@xlink:title) != ''"><xsl:value-of select="./@xlink:title" /></xsl:when>
                            <xsl:otherwise><xsl:value-of select="../unittitle[1]/text()" /></xsl:otherwise>
                        </xsl:choose>
                    </xsl:variable>
                    <!-- if thumbnail exists -->
                    <xsl:variable name="thumbnailHref">
                        <xsl:choose>
                            <xsl:when test="$thumbnail">
                                <xsl:choose>
                                    <xsl:when test="$numberOfThumbnails >= $linkPosition">
                                        <xsl:value-of select="$thumbnail[$linkPosition]/@xlink:href" />
                                    </xsl:when>
                                    <xsl:otherwise>
                                        <xsl:value-of select="$thumbnail[1]/@xlink:href" />
                                    </xsl:otherwise>
                                </xsl:choose>
                            </xsl:when>
                        </xsl:choose>
                    </xsl:variable>
                    <xsl:variable name="positionInList" select="position()"/>
                    <xsl:variable name="positionInListThumbnail" select="count(preceding-sibling::dao[@xlink:title='thumbnail']) + 1"/>
                    <mets:div ID="{concat('PHYS_', $positionInListThumbnail + 1)}" ORDER="{$positionInListThumbnail}" LABEL="{$title}">
                        <mets:fptr FILEID="{concat('FILE_', $positionInList, '_', me:get_role(.))}"/>
                        <xsl:if test="$thumbnailHref">
                            <mets:fptr FILEID="{concat('FILE_', $positionInListThumbnail, '_THUMB')}"/>
                        </xsl:if>
                    </mets:div>
                </xsl:for-each>
                <xsl:for-each select="did/dao[@xlink:title != 'thumbnail' or not(@xlink:title)][@xlink:role='UNSPECIFIED' or not(@xlink:role)]">
                    <xsl:variable name="linkPosition" select="position()" />
                    <xsl:variable name="title">
                        <xsl:choose>
                            <xsl:when test="normalize-space(@xlink:title) != ''"><xsl:value-of select="./@xlink:title" /></xsl:when>
                            <xsl:otherwise><xsl:value-of select="../unittitle[1]/text()" /></xsl:otherwise>
                        </xsl:choose>
                    </xsl:variable>
                    <!-- if thumbnail exists -->
                    <xsl:variable name="thumbnailHref">
                        <xsl:choose>
                            <xsl:when test="$thumbnail">
                                <xsl:choose>
                                    <xsl:when test="$numberOfThumbnails >= $linkPosition">
                                        <xsl:value-of select="$thumbnail[$linkPosition]/@xlink:href" />
                                    </xsl:when>
                                    <xsl:otherwise>
                                        <xsl:value-of select="$thumbnail[1]/@xlink:href" />
                                    </xsl:otherwise>
                                </xsl:choose>
                            </xsl:when>
                        </xsl:choose>
                    </xsl:variable>
                    <xsl:variable name="positionInList" select="position()"/>
                    <xsl:variable name="positionInListThumbnail" select="count(preceding-sibling::dao[@xlink:title='thumbnail']) + 1"/>
                    <mets:div ID="{concat('PHYS_', $positionInListThumbnail + 1)}" ORDER="{$positionInListThumbnail}" LABEL="{$title}">
                        <mets:fptr FILEID="{concat('FILE_', $positionInList, '_', me:get_role(.))}"/>
                        <xsl:if test="$thumbnailHref">
                            <mets:fptr FILEID="{concat('FILE_', $positionInListThumbnail, '_THUMB')}"/>
                        </xsl:if>
                    </mets:div>
                </xsl:for-each>
            </mets:div>
        </mets:structMap>
    </xsl:template>

    <!--Create structlink section-->
    <xsl:template name="structLink">
        <!-- Work todo: Have this???? -->
        <mets:structLink>
            <xsl:variable name="count_start" select="@c_pos"/>
            <xsl:variable name="count_start2"
                          select="descendant-or-self::node()[@c_dao_pos][1]/@c_dao_pos"/>
            <!-- problems with [descendant::node()='daogrp'] - when have 'did' and 'daogrp'?-->
            <!-- Work todo: Get this to work?? -->
            <xsl:for-each select="descendant-or-self::node()[me:is_element_c(.)=true()]">
                <xsl:variable name="count_end" select="@c_pos"/>
                <xsl:for-each
                        select="descendant-or-self::node()[me:is_correct_dao(.)=true() or @level=$settings/split_ead]">
                    <xsl:variable name="count_end2" select="@c_dao_pos"/>
                    <mets:smLink xlink:from="{concat('LOG_',$count_end - $count_start +1)}"
                                 xlink:to="{concat('PHYS_',$count_end2 - $count_start2 +1)}"/>
                </xsl:for-each>
            </xsl:for-each>
        </mets:structLink>
    </xsl:template>

    <!--descriptive Metadata: MODS-->
    <!-- Work todo: This can be used but needs manually correction for all -->
    <xsl:template name="dmdSec_mods">
        <mets:dmdSec ID="{concat('DMDLOG_',position())}">
            <mets:mdWrap MDTYPE="MODS">
                <mets:xmlData>
                    <!--<mods:mods>
                        <mods:identifier displayLabel="urn">URN...</mods:identifier>
                        <mods:originInfo>
                            <mods:dateCreated>
                                <xsl:value-of select="did/unitdate"/>
                            </mods:dateCreated>
                        </mods:originInfo>
                        <mods:titleInfo>
                            <mods:title>
                                <xsl:value-of select="concat(did/unittitle,' (',@level,')')"/>
                            </mods:title>
                        </mods:titleInfo>
                        <mods:classification>
                            <xsl:value-of select="did/unitid"/>
                        </mods:classification>
                    </mods:mods>-->
                </mets:xmlData>
            </mets:mdWrap>
        </mets:dmdSec>
    </xsl:template>

    <!--descriptive Metadata: EAD-->
    <!-- For EAD inforamtion in the METS -->
    <!-- Work todo: Get this to work???? -->
    <xsl:template name="dmdSec_ead">
        <xsl:param name="metadata_collector"/>
        <!--        <xsl:param name="identifier"/>-->
        <xsl:param name="position"/>
        <mets:dmdSec ID="{concat('DMDLOG_',position())}">
            <mets:mdWrap MDTYPE="EAD">
                <mets:xmlData>
                    <!--ead-header-->
                    <ead:ead xmlns:ead="urn:isbn:1-931666-22-9">
                        <ead:eadheader>
                            <ead:eadid/>
                            <ead:filedesc>
                                <ead:titlestmt>
                                    <ead:titleproper/>
                                </ead:titlestmt>
                            </ead:filedesc>
                        </ead:eadheader>
                        <ead:archdesc level="collection">
                            <ead:did>
                                <ead:repository/>
                            </ead:did>
                            <ead:dsc>
                                <xsl:choose>
                                    <!-- For the different sections-->
                                    <xsl:when test="upper-case($settings/metadata)='EAD_LONG'">
                                        <xsl:call-template name="dmdSec_ead_long">
                                            <xsl:with-param name="metadata_collector"
                                                            select="$metadata_collector"/>
                                            <xsl:with-param name="count_anc"
                                                            select="count(self::node()[me:is_element_c(.)=true()]/ancestor-or-self::node()) -4"
                                                    />
                                        </xsl:call-template>
                                    </xsl:when>
                                    <xsl:when test="upper-case($settings/metadata)='EAD_MEDIUM'">
                                        <xsl:call-template name="dmdSec_ead_medium">
                                            <xsl:with-param name="count_anc"
                                                            select="count(self::node()[me:is_element_c(.)=true()]/ancestor-or-self::node()) -4"/>
                                            <xsl:with-param name="position" select="$position"/>
                                            <xsl:with-param name="metadata_collector"
                                                            select="$metadata_collector"/>
                                        </xsl:call-template>
                                    </xsl:when>
                                    <xsl:when test="upper-case($settings/metadata)='EAD_SHORT'">
                                        <xsl:call-template name="dmdSec_ead_short"
                                                > </xsl:call-template>
                                    </xsl:when>
                                    <xsl:when test="upper-case($settings/metadata)='EAD_MINIMUM'">
                                        <xsl:call-template name="dmdSec_ead_minimum"
                                                > </xsl:call-template>
                                    </xsl:when>
                                    <xsl:otherwise>
                                        <xsl:message>Values incorrect use:
                                            mods/ead_long/ead_medium/ead_short/ead_minimum</xsl:message>
                                    </xsl:otherwise>
                                </xsl:choose>
                            </ead:dsc>
                        </ead:archdesc>
                    </ead:ead>
                </mets:xmlData>
            </mets:mdWrap>
        </mets:dmdSec>
    </xsl:template>

    <!-- Structure of c-elements -->
    <xsl:template name="create_branch">
        <xsl:param name="counter"/>
        <xsl:param name="metadata_collector"/>
        <xsl:for-each select="$metadata_collector/child::node()[position()=$counter]">
            <c>
                <xsl:copy-of select="@*"/>
                <xsl:copy-of select="child::node()"/>
                <xsl:call-template name="create_branch">
                    <xsl:with-param name="counter" select="$counter +1"/>
                    <xsl:with-param name="metadata_collector" select="$metadata_collector"/>
                </xsl:call-template>
            </c>
        </xsl:for-each>
    </xsl:template>

    <!--descriptive Metadata: EAD_long-->
    <xsl:template name="dmdSec_ead_long">
        <!--        <xsl:param name="identifier"/>-->
        <xsl:param name="metadata_collector"/>
        <xsl:param name="count_anc"/>

        <xsl:call-template name="create_branch">
            <xsl:with-param name="counter" select="1"/>
            <xsl:with-param name="metadata_collector">
                <xsl:copy-of select="$metadata_collector"/>
                <xsl:element name="{if(local-name(.)='archdesc') then 'ead:archdesc' else 'ead:c'}"
                             namespace="urn:isbn:1-931666-22-9">
                    <xsl:copy-of
                            select="@*[local-name(.)!='c_pos' and local-name(.)!='c_dao_pos' and local-name(.)!='c_meta']"/>
                    <xsl:copy-of select="child::node()[me:is_element_c(.)=false()][ me:is_correct_dao(.)=false()]"
                            />
                </xsl:element>
            </xsl:with-param>
        </xsl:call-template>
    </xsl:template>

    <!--descriptive Metadata: EAD_short-->

    <xsl:template name="dmdSec_ead_short">
        <xsl:for-each select="self::node()[me:is_element_c(.)=true()]">
            <xsl:element name="{if(local-name(.)='archdesc') then 'ead:archdesc' else 'ead:c'}"
                         namespace="urn:isbn:1-931666-22-9">
                <xsl:copy-of
                        select="@*[local-name(.)!='c_pos' and local-name(.)!='c_dao_pos' and local-name(.)!='c_meta']"/>
                <xsl:copy-of select="child::node()[me:is_element_c(.)=false()][ me:is_correct_dao(.)=false()]"/>
            </xsl:element>
        </xsl:for-each>
    </xsl:template>

    <!--descriptive Metadata: EAD_medium = EAD_long and EAD_short-->

    <xsl:template name="dmdSec_ead_medium">
        <xsl:param name="metadata_collector"/>
        <!--        <xsl:param name="identifier"/>-->
        <xsl:param name="count_anc"/>
        <xsl:param name="position"/>

        <xsl:choose>
            <xsl:when test="$position=1">
                <xsl:call-template name="dmdSec_ead_long">
                    <xsl:with-param name="count_anc" select="$count_anc"/>
                    <xsl:with-param name="metadata_collector" select="$metadata_collector"/>
                    <!--                    <xsl:with-param name="identifier" select="$identifier"/>-->
                </xsl:call-template>
            </xsl:when>

            <xsl:otherwise>
                <xsl:call-template name="dmdSec_ead_short"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <!--descriptive Metadata: EAD_minimum-->

    <xsl:template name="dmdSec_ead_minimum">
        <xsl:for-each select="self::node()[me:is_element_c(.)=true()]">
            <xsl:element name="{if(local-name(.)='archdesc') then 'ead:archdesc' else 'ead:c'}"
                         namespace="urn:isbn:1-931666-22-9">
                <xsl:copy-of
                        select="@*[local-name(.)!='c_pos' and local-name(.)!='c_dao_pos' and local-name(.)!='c_meta']"/>
                <did>
                    <xsl:copy-of select="did//unittitle"/>
                    <xsl:copy-of select="did//unitid"/>
                    <xsl:copy-of select="did//unitdate"/>
                </did>
            </xsl:element>
        </xsl:for-each>
    </xsl:template>

    <!--Create the new EAD -->

    <xsl:template name="create_new_ead">
        <xsl:result-document href="{concat('output/', 'ead_mets_',//eadid,'.xml')}" format="my-xhtml-output">
            <xsl:apply-templates select="node()|@*" mode="delete_double_ead_mets"/>
        </xsl:result-document>
    </xsl:template>

    <!--delete_double_ead_mets-->
    <xsl:template match="node()|@*" mode="delete_double_ead_mets">
        <xsl:copy>
            <xsl:copy-of select="@*[local-name(.)!='c_pos' and local-name(.)!='c_dao_pos' and local-name(.)!='c_meta']"/>
            <xsl:apply-templates select="node()|@*" mode="delete_double_ead_mets"/>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="dao" mode="delete_double_ead_mets">
        <!-- Work todo: the wrong id gets collected so the EAD dao don't point to the correct METS document -->
        <xsl:variable name="ger" select="me:get_id(../..)"/>
        <xsl:if test="local-name(.)='dao'">
            <!--xsl:copy-of select="count(preceding-sibling::dao) "/-->
            <xsl:if test="count(preceding-sibling::dao)=0">
                <xsl:copy>
                    <xsl:attribute name="xlink:role" select="'METS'"/>
                    <xsl:attribute name="xlink:type" select="'simple'"/>
                    <xsl:attribute name="xlink:href"
                                   select="concat($settings/path_mets,$ger,'.xml')"/>
                </xsl:copy>
            </xsl:if>
        </xsl:if>
    </xsl:template>

</xsl:stylesheet>
