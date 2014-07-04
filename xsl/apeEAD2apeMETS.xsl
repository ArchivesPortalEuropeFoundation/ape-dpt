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
                xpath-default-namespace="urn:isbn:1-931666-22-9" version="2.0" exclude-result-prefixes=" me">

    <!-- Tabel for fileGrp @USE -->
    <!-- This needs to be manually updated and follow the apeMETS profile USE values-->
    <!-- Work todo: there are in some cases other attributes used they also needs to be handled -->
    <xsl:strip-space elements="did" />

    <!-- Path to where the METS will be found -->
    <!-- This needs to be manually updated -->
    <xsl:variable name="link_to_mets">http://www.link_to_mets.ape/mets</xsl:variable>

    <xsl:output name="my-xhtml-output" method="xml" encoding="UTF-8" indent="yes"/>

    <!-- Settings -->
    <xsl:variable name="settings">
        <!-- How will the METS document be? Minimum gives all the necessary to make a METS document following the apeMETS profile -->
        <!-- Work todo: Maximum is currently not fully supported -->
        <!-- Values: mets_minimum/mest_maximum-->
        <mets_profil>mets_minimum</mets_profil>

        <!-- Value for dmdsec creation -->
        <!-- Values: mods/ead_long/ead_medium/ead_short/ead_minimum-->
        <!-- Work todo: should we have this??? -->
        <metadata>ead_short</metadata>

        <split_ead>fonds</split_ead>

        <!-- Create the filename and path to the METS document -->
        <path_mets>
            <xsl:value-of select="concat($link_to_mets, substring-before(max(tokenize(document-uri(.), '/')), '.xml'), '/')"/>
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
        <!-- These values needs to be manually updated -->
        <!-- Work todo: This list can be extended -->
        <type_anchor>Finding aid</type_anchor>
        <type_structure>Archival</type_structure>
        <mimetype>image/jpg</mimetype>
        <mets_name>Institution name</mets_name>
    </xsl:variable>

    <!-- Get highest level (generally: collection, temporary in apeEAD export: fonds)  -->
    <xsl:variable name="highlev">
        <xsl:value-of select="/ead/archdesc/dsc/c[@level]"/>
    </xsl:variable>

    <!-- Functions -->

    <xsl:function name="me:all_c">
        <!-- Is it a c? -->
        <xsl:param name="c_name" />
        <xsl:value-of select="if(local-name($c_name)='c') then true() else false()" />
    </xsl:function>

    <!-- Create label for logical structmap -->
    <xsl:function name="me:get_label">
        <!-- Work todo: Which values should be use? The else part is bad, it should be using something else (not existing in apeEAD) -->
        <xsl:param name="context"/>
        <xsl:value-of select="if (normalize-space($context/did/unittitle)!='') then concat($context/did/unittitle,' (',$context/@level,')') else concat($context/daodesc/list/item/name,' (Picture)')" />
    </xsl:function>

    <!-- Create the href -->
    <xsl:function name="me:get_href">
        <!-- Work todo: Check that all versions of apeEAD are working -->
        <xsl:param name="context"/>
        <xsl:value-of select="if(normalize-space($context/@xlink:href)!='') then $context/@xlink:href else $context/@href" />
    </xsl:function>

    <!-- Check the role attribute -->
    <xsl:function name="me:get_role">
        <!-- Work todo: Check that all versions of apeEAD are working -->
        <!-- Work todo: and also get the different use of other dao attributes into this! -->
        <!-- Work todo: make sure it handles the @USE in METS correctly -->
        <xsl:param name="context"/>
        <xsl:value-of select="if(normalize-space($context/@xlink:role)!='') then $context/@xlink:role else $context/@role" />
    </xsl:function>

    <!-- Check the title attribute -->
    <xsl:function name="me:get_title">
        <!-- Work todo: Check that all versions of apeEAD are working -->
        <xsl:param name="context"/>
        <xsl:value-of select="if(normalize-space($context/@xlink:title)!='') then $context/@xlink:title else $context/@title" />
    </xsl:function>

    <!-- Check the daogrp and daoloc  -->
    <xsl:function name="me:all_dao">
        <xsl:param name="dao_check"/>
        <xsl:value-of select="if(local-name($dao_check)='dao') then if(normalize-space($dao_check/me:get_href(.))!='') then true() else false() else false()" />
    </xsl:function>

    <!-- Check the link to the digital object -->
    <!--<xsl:function name="me:only_dao">-->
    <!--<xsl:param name="dao"/>-->
    <!--<xsl:value-of-->
    <!--select="-->
    <!--if(upper-case($settings/only_dao)='YES') then if($dao/descendant-or-self::node()[me:all_dao(.)=true()]) then true() else false() else true()"-->
    <!--/>-->
    <!--</xsl:function>-->

    <!-- If no ID attribute exist in the c an internal one is created -->
    <xsl:function name="me:get_id">
        <xsl:param name="get_id"/>
        <xsl:value-of select="if(normalize-space($get_id/@id)!='') then $get_id/@id else generate-id($get_id)" />
    </xsl:function>

    <!-- Variables for the EAD creation -->


    <!-- Get the ordernumber from the value list of order -->
    <!--<xsl:variable name="level" select="$level_settings/c[@level=$settings/split_ead]/@order"/>-->

    <!-- Create the anchor -->
    <!--<xsl:function name="me:get_level_pos_anchor">-->
    <!--<xsl:param name="context"/>-->
    <!--<xsl:param name="level"/>-->
    <!--<xsl:value-of-->
    <!--select="-->
    <!--if($level_settings/c[@level=$context/@level]/@order&lt;$level) then true() else-->
    <!--if($level_settings/c[@level=$context/@level]/@order=$level_settings/c[@level=$context/child::node()[me:all_c2(.)]/@level]/@order) then-->
    <!--if($settings/split_ead=$context/@level) then-->
    <!--if($settings/last_level='last') then-->
    <!--true() else false()  else false()  else false()"-->
    <!--/>-->
    <!--</xsl:function>-->

    <!-- Work with the splitting -->
    <!--<xsl:function name="me:get_level_pos_split">-->
    <!--<xsl:param name="context"/>-->
    <!--<xsl:param name="level"/>-->
    <!--<xsl:value-of-->
    <!--select="-->
    <!--if($settings/last_level='last') then-->
    <!--if($settings/split_ead=$context/@level) then-->
    <!--if($level_settings/c[@level=$context/@level]/@order=$level_settings/c[@level=$context/child::node()[me:all_c2(.)]/@level]/@order) then-->
    <!--false() else true()  else true()  else true()"-->
    <!--/>-->
    <!--</xsl:function>-->

    <!-- Create two lists to work with one for all_c=true() and one for check_dao=true()-->
    <xsl:param name="list_pos">
        <xsl:for-each select="//descendant-or-self::node()[me:all_dao(.)=true()]">
            <c_dao c_dao_pos="{position()}" id="{generate-id(.)}"/>
        </xsl:for-each>
    </xsl:param>

    <!-- Use the two lists of objects and create one list -->
    <xsl:template name="combine_file_list">
        <!-- Just a temporary list -->
        <xsl:variable name="ger" select="generate-id(.)"/>
        <xsl:copy>
            <xsl:copy-of select="@*"/>
            <xsl:for-each
                    select="$list_pos/node()[@id=$ger]/attribute::node()[not(local-name(.)='id')]">
                <xsl:attribute name="id">
                    <xsl:value-of select="$ger"/>
                </xsl:attribute>
                <xsl:attribute name="{local-name(.)}" select="."/>
            </xsl:for-each>
            <xsl:for-each select="child::node()">
                <xsl:call-template name="combine_file_list"/>
            </xsl:for-each>
        </xsl:copy>
    </xsl:template>

    <xsl:variable name="file_withpos">
        <xsl:for-each select="child::node()">
            <xsl:call-template name="combine_file_list"/>
        </xsl:for-each>
    </xsl:variable>

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

    <!-- Template to create full version METS -->
    <!-- Work todo: Check and see thats its gets correct -->
    <xsl:template match="node()|@*" mode="create_mets_full">
        <xsl:apply-templates select="node()|@*" mode="create_mets_full"/>
    </xsl:template>

    <!--<xsl:template match="node()|@*" mode="create_mets_simple">-->
    <!--<xsl:apply-templates select="node()|@*" mode="create_mets_simple"/>-->
    <!--</xsl:template>-->

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

    <!-- Create METS anchors -->
    <xsl:template
            match="node()"
            mode="create_mets_full">

        <xsl:apply-templates mode="create_mets_full">
            <xsl:with-param name="metadata_collector">
                <xsl:element name="{if(local-name(.)='archdesc') then 'archdesc' else 'c'}">
                    <xsl:copy-of
                            select="@*[local-name(.)!='c_pos' and local-name(.)!='c_dao_pos' and local-name(.)!='c_meta']"/>
                    <xsl:copy-of select="child::node()[me:all_c(.)=false()]"/>
                </xsl:element>
            </xsl:with-param>
        </xsl:apply-templates>
    </xsl:template>

    <xsl:template
            match="node()"
            mode="create_mets_full">
        <xsl:param name="metadata_collector"/>

        <xsl:variable name="level2">
            <xsl:value-of select="@level"/>
        </xsl:variable>

        <xsl:apply-templates mode="create_mets_full">
            <xsl:with-param name="metadata_collector">
                <xsl:copy-of select="$metadata_collector"/>
                <xsl:element name="{if(local-name(.)='archdesc') then 'archdesc' else 'c'}">
                    <xsl:copy-of
                            select="@*[local-name(.)!='c_pos' and local-name(.)!='c_dao_pos' and local-name(.)!='c_meta']"/>
                    <xsl:copy-of select="child::node()[me:all_c(.)=false()]"/>
                </xsl:element>
            </xsl:with-param>
        </xsl:apply-templates>
    </xsl:template>

    <!-- Create logical structmap anchors -->
    <xsl:template name="structMap_log_anchor">
        <!-- Work todo: Check the function -->
        <mets:structMap TYPE="LOGICAL">
            <mets:div ID="LOG_1" ADMID="AMD1" DMDID="DMDLOG_1" LABEL="{me:get_label(.)}">
                <mets:mptr LOCTYPE="URL"
                           xlink:href="{concat($settings/path_mets,if(name(parent::node())!='archdesc') then parent::node()/@id else concat('archdesc_',//c/daogrp[normalize-space(daoloc/@xlink:href)!='']
                        //eadid,'_',generate-id(parent::node())),'.xml')}"
                        />
                <xsl:for-each select="node()[me:all_c(.)=true()][me:all_dao(.)=true()]">
                    <mets:div ID="{concat('LOG_',position()+1)}"
                              DMDID="{concat('DMDLOG_',position()+1)}"
                              TYPE="{$value_settings/type_anchor}" LABEL="{me:get_label(.)}">
                        <mets:mptr LOCTYPE="URL"
                                   xlink:href="{concat($settings/path_mets,@id,'.xml')}"/>
                    </mets:div>
                </xsl:for-each>
            </mets:div>
        </mets:structMap>
    </xsl:template>

    <!-- Create a full version of apeMETS -->
    <xsl:template
            match="node()"
            mode="create_mets_full">

        <xsl:param name="metadata_collector"/>
        <xsl:for-each select=".">
            <xsl:message>We are creating a file: <xsl:value-of select="me:get_id(.)"/></xsl:message>
            <xsl:result-document href="{concat(me:get_id(.),'.xml')}" format="my-xhtml-output">
                <mets:mets xmlns:mets="http://www.loc.gov/METS/"
                           xmlns:xlink="http://www.w3.org/1999/xlink"
                           xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                           xsi:schemaLocation="http://www.loc.gov/METS/ apeMETS.xsd
                    urn:isbn:1-931666-22-9 http://www.loc.gov/ead/ead.xsd"
                           PROFILE="apeMETS">
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

    <xsl:template match="c[did/dao]" mode="create_mets_simple">
        <xsl:for-each select=".">
            <xsl:message>We are creating a file: <xsl:value-of select="me:get_id(.)"/></xsl:message>
            <xsl:result-document href="{concat('output/', me:get_id(.),'.xml')}" format="my-xhtml-output">
                <mets:mets xmlns:mets="http://www.loc.gov/METS/"
                           xmlns:xlink="http://www.w3.org/1999/xlink"
                           xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                           xsi:schemaLocation="http://www.loc.gov/METS/ apeMETS.xsd
                    urn:isbn:1-931666-22-9 http://www.loc.gov/ead/ead.xsd"
                           PROFILE="apeMETS">
                    <xsl:call-template name="metsHdr"/>
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
                            <mets:div ID="{concat('LOG_','1')}" ADMID="AMD1"
                                DMDID="{concat('DMDLOG_','1')}" TYPE="map"> </mets:div>
                        </mets:div>
                    </mets:structMap>-->
                    <!-- Work todo: o structlink in profile should we have it here? -->
                    <!--<mets:structLink>
                        <mets:smLink xlink:from="{concat('LOG_','1')}"
                            xlink:to="{concat('PHYS_','1')}"/>
                    </mets:structLink>-->
                </mets:mets>
            </xsl:result-document>
            <xsl:apply-templates mode="create_mets_simple"/>
        </xsl:for-each>
    </xsl:template>

    <!--Mets header-->
    <xsl:template name="metsHdr">
        <mets:metsHdr CREATEDATE="{current-dateTime()}" LASTMODDATE="{current-dateTime()}">
            <mets:agent ROLE="CUSTODIAN">
                <mets:name>
                    <xsl:value-of select="$value_settings/mets_name"/>
                </mets:name>
            </mets:agent>
            <mets:altRecordID TYPE="apeID">
                <!-- Work todo: Should this be entered this way?? -->
                <xsl:value-of select="me:get_id(.)"/>
            </mets:altRecordID>
            <mets:metsDocumentID>
                <xsl:value-of select="concat(me:get_id(.),'.xml')"/>
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
        <!-- Save the tree in a variable so the for-each can work -->
        <xsl:variable name="tree_branch">
            <xsl:copy-of select="self::node()"/>
        </xsl:variable>
        <mets:fileSec>
            <!-- The logic for getting the information from the daogrp is not good -->
            <!-- Work todo: this needs refinement and adjustment to get it to work -->
            <!-- Function for getting the maximum amount of daolocs <test><xsl:value-of select="max(for $z in descendant-or-self::node()[local-name(.)='daogrp'] return count($z/daoloc))"/></test>-->
            <xsl:for-each
                    select="if(not(descendant-or-self::node()[local-name(.)='daogrp'])) then descendant-or-self::node()[local-name(.)='dao'][1] else descendant-or-self::node()[local-name(.)='daogrp'][1]/daoloc">
                <xsl:variable name="link_role" select="me:get_role(.)"/>
                <!-- Find the value in the table of filegrp of use values -->
                <!-- Work todo: get the reading of the use list to work -->
                <mets:fileGrp USE="DEFAULT">

                    <!--                    USE="{if(some $h in $file_grp_values/child::node() satisfies $h/me:get_role(.) = $link_role) then $file_grp_values/child::node()[me:get_role(.)=$link_role] else concat('DEFAULT_',position())}">-->
                    <!-- Get the filegrp content-->
                    <xsl:for-each
                            select="$tree_branch/descendant::node()/node()[me:all_dao(.)=true()]">
                        <xsl:if
                                test="self::node()[me:get_role(.)=$link_role] or daoloc[me:get_role(.)=$link_role]">
                            <!-- Work todo: Size is mandatory according to the profile but we need to get the value can it be done or does it require a change in the profile? -->
                            <!-- Work todo: mimetype is a fixed value it should be created by another look up this requires knowledge of all possible filetypes that will be used in apeEAD for dao's is that possible? Does this require a change in the profile? -->
                            <mets:file
                                    ID="{concat('FILE_',position(),'_',daoloc[me:get_role(.)=$link_role]/me:get_role(.),self::node()[me:get_role(.)=$link_role]/me:get_role(.))}"
                                    SIZE="0" MIMETYPE="{$value_settings/mimetype}">
                                <!-- These two needs to be switched manually -->
                                <!-- If daoloc's are used this should be used -->
                                <!--<mets:FLocat LOCTYPE="URL" xlink:type="simple"
                                    xlink:href="{concat(daoloc[me:get_role(.)=$link_role]/me:get_href(.),self::node()[me:get_role(.)=$link_role]/me:get_href(.))}"
                                />-->
                                <mets:FLocat LOCTYPE="URL" xlink:type="simple"
                                             xlink:href="{concat(dao[me:get_role(.)=$link_role]/me:get_href(.),self::node()[me:get_role(.)=$link_role]/me:get_href(.))}"
                                        />
                            </mets:file>
                        </xsl:if>
                    </xsl:for-each>
                </mets:fileGrp>
            </xsl:for-each>
        </mets:fileSec>
    </xsl:template>

    <!--structmap-->
    <!--create the logical map-->
    <!-- Work todo: Get the logical to work if we should have it?! -->

    <xsl:template name="structMap_log">
        <xsl:param name="identifier"/>
        <mets:structMap TYPE="LOGICAL">
            <mets:div TYPE="{$value_settings/type_structure}"
                      LABEL="{concat(parent::node()/did/unittitle,' (',parent::node()/@level,')')}">
                <xsl:if
                        test="normalize-space($settings/max_level)!=normalize-space($settings/split_ead)">
                    <mets:mptr LOCTYPE="URL"
                               xlink:href="{concat($settings/path_mets,parent::node()/me:get_id(.),'.xml')}"
                            />
                </xsl:if>
                <mets:div ID="{concat('LOG_','1')}" ADMID="AMD1" DMDID="{concat('DMDLOG_','1')}"
                          LABEL="{me:get_label(.)}"
                          TYPE="{$value_settings/type_structure}">
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
        <!--problems with [descendant::node()='daogrp'] - -->
        <!-- Work todo: get this to work if we should have it -->
        <xsl:for-each select="node()[me:all_c(.)=true()][me:all_dao(.)=true()]">
            <xsl:variable name="count_end" select="@c_pos"/>
            <xsl:variable name="count_meta_end"
                          select="if(@c_meta) then @c_meta else $count_meta_end"/>
            <mets:div ID="{concat('LOG_',$count_end - $count_start +1)}"
                      DMDID="{concat('DMDLOG_',$count_meta_end - $count_meta_start +1)}"
                      TYPE="{$value_settings/type_structure}" LABEL="{me:get_label(.)}">
                <xsl:if test="child::node()[me:all_c(.)=true()][me:all_dao(.)=true()]">
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
            <mets:div ID="{concat('PHYS_','1')}">
                <xsl:for-each select="did/dao">
                    <mets:div ID="{concat('PHYS_',position()+1)}" ORDER="{position()}" LABEL="{me:get_title(.)}">
                        <!-- if its mets_minimum leave the orderlabel out-->
                        <xsl:if test="upper-case($settings/mets_profil)!='METS_MINIMUM'">
                            <xsl:attribute name="ORDERLABEL"
                                           select="concat(position(),' - ',daodesc/list/item/name)"/>
                        </xsl:if>
                        <!-- position() has to be saved here, if daogrp and dao are mixed up-->
                        <xsl:variable name="pos" select="position()"/>
                        <!-- create a  file pointer for each daogrp or dao link-->
                        <xsl:choose>
                            <xsl:when test="local-name(.)='daogrp'">
                                <xsl:for-each select="self::node()/daoloc">
                                    <mets:fptr FILEID="{concat('FILE_',$pos,'_',me:get_role(.))}"/>
                                </xsl:for-each>
                            </xsl:when>
                            <xsl:when test="local-name(.)='dao'">
                                <mets:fptr FILEID="{concat('FILE_',$pos,'_',me:get_role(.))}"/>
                            </xsl:when>
                        </xsl:choose>
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
            <xsl:for-each select="descendant-or-self::node()[me:all_c(.)=true()]">
                <xsl:variable name="count_end" select="@c_pos"/>
                <xsl:for-each
                        select="descendant-or-self::node()[me:all_dao(.)=true() or @level=$settings/split_ead]">
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
                                                            select="count(self::node()[me:all_c(.)=true()]/ancestor-or-self::node()) -4"
                                                    />
                                        </xsl:call-template>
                                    </xsl:when>
                                    <xsl:when test="upper-case($settings/metadata)='EAD_MEDIUM'">
                                        <xsl:call-template name="dmdSec_ead_medium">
                                            <xsl:with-param name="count_anc"
                                                            select="count(self::node()[me:all_c(.)=true()]/ancestor-or-self::node()) -4"/>
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
                    <xsl:copy-of select="child::node()[me:all_c(.)=false()][ me:all_dao(.)=false()]"
                            />
                </xsl:element>
            </xsl:with-param>
        </xsl:call-template>
    </xsl:template>

    <!--descriptive Metadata: EAD_short-->

    <xsl:template name="dmdSec_ead_short">
        <xsl:for-each select="self::node()[me:all_c(.)=true()]">
            <xsl:element name="{if(local-name(.)='archdesc') then 'ead:archdesc' else 'ead:c'}"
                         namespace="urn:isbn:1-931666-22-9">
                <xsl:copy-of
                        select="@*[local-name(.)!='c_pos' and local-name(.)!='c_dao_pos' and local-name(.)!='c_meta']"/>
                <xsl:copy-of select="child::node()[me:all_c(.)=false()][ me:all_dao(.)=false()]"/>
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
        <xsl:for-each select="self::node()[me:all_c(.)=true()]">
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
        <xsl:if test="upper-case($settings/create_new_ead)!='YES_DELETE_EAD_METS_REDUNDANCE'">
            <xsl:message>Wrong value used. Use Value: yes_delete_ead_mets_redundance</xsl:message>
        </xsl:if>
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
