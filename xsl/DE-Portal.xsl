<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns="urn:isbn:1-931666-22-9" xmlns:xlink="http://www.w3.org/1999/xlink"
    xmlns:mr="func" xsi:schemaLocation="urn:isbn:1-931666-22-9 http://www.loc.gov/ead/ead.xsd http://www.w3.org/1999/xlink http://www.loc.gov/standards/xlink/xlink.xsd"
    xpath-default-namespace="urn:isbn:1-931666-22-9" version="2.0">

    <!--converted APE_version kann icht nicht angeben-->
    <xsl:import href="system/default-apeEAD.xsl"/>

    <xsl:param name="versionnb" select="'1.2'"/>

    <xsl:key name="key_id" match="//c" use="@id"/>

    <xsl:function name="mr:clear_id">
        <xsl:param name="elem"/>

        <xsl:value-of
            select="concat('DDB_',replace($elem,'[^A-Z|_|a-z|&#xC0;-&#xD6;|&#xD8;-&#xF6;|&#xF8;-&#x2FF;|&#x370;-&#x37D;|&#x37F;-&#x1FFF;|&#x200C;-&#x200D;|&#x2070;-&#x218F;|&#x2C00;-&#x2FEF;|&#x3001;-&#xD7FF;|&#xF900;-&#xFDCF;|&#xFDF0;-&#xFFFD;|&#x10000;-&#xEFFFF;|\-|\.|0-9|&#xB7;|&#x0300;-&#x036F;|&#x203F;-&#x2040;]','_'))"
        />
    </xsl:function>

    <xsl:template match="/">
            <xsl:apply-templates select="ead[.//archdesc[@type='Findbuch']]" mode="Findbuch"/>
            <xsl:apply-templates select="ead[.//archdesc[@type='Tektonik']]" mode="Tektonik"/>
    </xsl:template>

    <xsl:template match="ead" mode="Findbuch">
        <!--<xsl:result-document href="{concat(//@mainagencycode,'/Findbuch/',//@mainagencycode,'_',mr:clear_id(descendant::c[1]/@id),'.xml')}">-->
        <ead xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns="urn:isbn:1-931666-22-9"
            xsi:schemaLocation="urn:isbn:1-931666-22-9 http://www.archivesportaleurope.net/Portal/profiles/apeEAD.xsd http://www.w3.org/1999/xlink http://www.loc.gov/standards/xlink/xlink.xsd"
            audience="external">
            <eadheader countryencoding="iso3166-1" dateencoding="iso8601" langencoding="iso639-2b" repositoryencoding="iso15511" scriptencoding="iso15924">
                <eadid identifier="{concat(//@mainagencycode,'_',mr:clear_id(descendant::c[1]/@id))}" countrycode="DE" mainagencycode="{//@mainagencycode}">
                    <xsl:if test="normalize-space(descendant::c[1]/otherfindaid/extref/@xlink:href)!=''">
                        <xsl:attribute name="url" select="descendant::c[1]/otherfindaid/extref/@xlink:href"/>
                    </xsl:if>

                    <xsl:value-of select="mr:clear_id(descendant::c[1]/@id)"/>
                </eadid>
                <filedesc>
                    <titlestmt>
                        <titleproper encodinganalog="245">
                            <xsl:value-of select="//titleproper"/>
                        </titleproper>
                    </titlestmt>
                </filedesc>
                <profiledesc>
                    <creation>
                        <xsl:value-of select="//creation/date"/>
                    </creation>
                </profiledesc>
                <revisiondesc>
                    <change>
                        <date/>
                        <item>Converted_EADDDB2apeEAD_version_<xsl:value-of select="$versionnb"/></item>
                    </change>
                </revisiondesc>
            </eadheader>
            <archdesc level="fonds" type="inventory" encodinganalog="3.1.4" relatedencoding="ISAD(G)v2">
                <did>
                    <xsl:apply-templates select="descendant::c[@level='collection']/child::did/child::node()" mode="Findbuch"/>
                    <repository>
                        <xsl:value-of select="//archdesc/did/repository/corpname/text()"/>
                        <xsl:copy-of select="//archdesc/did/repository/address"/>
                        <xsl:copy-of select="//archdesc/did/repository/extref"/>
                    </repository>
                </did>
                <xsl:apply-templates select="descendant::c[@level='collection']/child::node()[not(self::text())][not(local-name(.)=('c','did'))]" mode="Findbuch"/>
                <dsc>
                    <xsl:apply-templates select="descendant::c[@level='collection']/child::c" mode="Findbuch"/>
                </dsc>
            </archdesc>
        </ead>
        <!--</xsl:result-document>-->
    </xsl:template>
    
    <xsl:template match="ead" mode="Tektonik">
        <!--<xsl:result-document href="{concat(//@mainagencycode,'/',mr:clear_id(descendant::c[1]/@id),'.xml')}">-->
        <ead xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns="urn:isbn:1-931666-22-9"
            xsi:schemaLocation="urn:isbn:1-931666-22-9 http://www.archivesportaleurope.net/Portal/profiles/apeEAD.xsd http://www.w3.org/1999/xlink http://www.loc.gov/standards/xlink/xlink.xsd"
            audience="external">
            <eadheader countryencoding="iso3166-1" dateencoding="iso8601" langencoding="iso639-2b" repositoryencoding="iso15511" scriptencoding="iso15924">
                <eadid identifier="{concat(//@mainagencycode,'_',descendant::c[1]/@id)}" countrycode="DE" mainagencycode="{//@mainagencycode}">
                    <xsl:if test="normalize-space(descendant::c[1]/otherfindaid/extref/@xlink:href)!=''">
                        <xsl:attribute name="url" select="descendant::c[1]/otherfindaid/extref/@xlink:href"/>
                    </xsl:if>
                    <xsl:value-of select="descendant::c[1]/@id"/>
                </eadid>
                <filedesc>
                    <titlestmt>
                        <titleproper>
                            <xsl:value-of select="//titleproper"/>
                        </titleproper>
                    </titlestmt>
                </filedesc>
                <!--<profiledesc>
                    <creation>
                        <xsl:value-of select="//creation/date"/>
                    </creation>
                </profiledesc>-->
                <revisiondesc>
                    <change>
                        <date/>
                        <item>Converted_EADDDB2apeEAD_version_<xsl:value-of select="$versionnb"/></item>
                    </change>
                </revisiondesc>
            </eadheader>
            <archdesc level="fonds" type="holdings_guide" encodinganalog="3.1.4" relatedencoding="ISAD(G)v2">
                <did>
                    <xsl:apply-templates select="descendant::c[@level='collection']/child::did/child::node()[not(local-name(.)='repository')]" mode="Tektonik"/>
                    <!-- <repository>
                        <xsl:copy-of select="//archdesc/did/repository/address"/>
                        <xsl:copy-of select="//archdesc/did/repository/extref"/>
                    </repository>-->
                </did>
                <xsl:apply-templates select="descendant::c[@level='collection']/child::node()[not(self::text())][not(local-name(.)=('c','did'))]" mode="Tektonik"/>
                <dsc>
                    <xsl:apply-templates select="descendant::c[@level='collection']/child::c" mode="Tektonik"/>
                </dsc>
            </archdesc>
        </ead>
        <!--</xsl:result-document>-->
    </xsl:template>

    <xsl:template match="c" mode="Findbuch">
        <c>
            <xsl:choose>
                <xsl:when test="normalize-space(@id)=''"/>
                <xsl:when test="count(key('key_id',@id))&gt;1">
                    <xsl:attribute name="id" select="concat(mr:clear_id(@id),'_',generate-id(.))"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:attribute name="id" select="mr:clear_id(@id)"/>
                </xsl:otherwise>

            </xsl:choose>
            <xsl:apply-templates select="@*[local-name(.)!='id']" mode="Findbuch"/>
            <xsl:call-template name="encoding_isadg"/>
            <did>
                <xsl:apply-templates select="did/child::node()"  mode="Findbuch"/>
                <xsl:apply-templates select="daogrp" mode="dao_findbuch"/>
            </did>
            <xsl:apply-templates select="did/abstract" mode="enthaeltvermerke_findbuch"/>
            <xsl:apply-templates select="child::node()[not(local-name(.)=('did','daogrp'))]" mode="Findbuch"/>
        </c>

    </xsl:template>

    <!--<xsl:template match="c[not(did/unittitle[normalize-space(.)!=''])]" mode="Findbuch"/>-->

    <xsl:template match="daogrp" mode="Findbuch"/>

    <xsl:template match="name[parent::origination]" mode="Findbuch">
        <name>
            <xsl:apply-templates select="@*" mode="Findbuch"/>
            <xsl:apply-templates select="text()[normalize-space(.)!='']" mode="create_hyphen"/>
        </name>
    </xsl:template>

    <xsl:template match="extent|materialspec|dimensions|language|head" mode="Findbuch">
        <xsl:element name="{local-name(.)}">
            <xsl:apply-templates select="text()[normalize-space(.)!='']" mode="create_hyphen"/>
        </xsl:element>
    </xsl:template>
    
    <xsl:template match="unitdate" mode="Findbuch">
        <unitdate>
            <xsl:call-template name="encoding_isadg"/>
            <xsl:apply-templates select="text()[normalize-space(.)!='']" mode="create_hyphen"/>
        </unitdate>
    </xsl:template>

    <xsl:template match="physdesc" mode="Findbuch">
        <physdesc>
            <xsl:call-template name="encoding_isadg"/>
            <xsl:apply-templates select="text()[normalize-space(.)!='']" mode="create_hyphen"/>
            <xsl:apply-templates select="child::node()[not(self::text())][not(local-name(.)='lb')]" mode="Findbuch"/>
        </physdesc>
    </xsl:template>

    <xsl:template match="genreform[parent::physdesc][normalize-space(.)='' and normalize-space(@normal)='']" mode="Findbuch"/>

    <xsl:template match="genreform[parent::physdesc][normalize-space(.)!='' or normalize-space(@normal)!='']" mode="Findbuch">
        <xsl:for-each select="@normal[normalize-space(.)!=''][normalize-space(.)!=normalize-space(parent::node()/text())]">
            <genreform>
                <xsl:value-of select="."/>
            </genreform>
        </xsl:for-each>

        <xsl:if test="normalize-space(.)!=''">
            <genreform>
                <xsl:apply-templates select="text()[normalize-space(.)!='']" mode="create_hyphen"/>
            </genreform>
        </xsl:if>
    </xsl:template>

    <xsl:template match="daogrp" mode="dao_findbuch">

        <xsl:choose>
            <xsl:when test="daoloc[@xlink:href/starts-with(.,'http://')][@xlink:role='externer_viewer']">
                <dao xlink:title="{daodesc/list/item/name}">
                    <xsl:copy-of select="daoloc[@xlink:role='externer_viewer']/@xlink:href"/>
                    <xsl:attribute name="xlink:role">
                        <xsl:apply-templates select="daodesc/list/item/genreform" mode="edm_type"/>
                    </xsl:attribute>
                </dao>
            </xsl:when>

            <xsl:when test="daoloc[@xlink:href/starts-with(.,'http://')][@xlink:role='image_full']">
                <dao xlink:title="{daodesc/list/item/name}">
                    <xsl:copy-of select="daoloc[@xlink:role='image_full']/@xlink:href"/>
                    <xsl:attribute name="xlink:role">
                        <xsl:apply-templates select="daodesc/list/item/genreform" mode="edm_type"/>
                    </xsl:attribute>
                </dao>
            </xsl:when>
            <xsl:when test="daoloc[not(@xlink:href/starts-with(.,'http://'))][normalize-space(ancestor::c[1]/otherfindaid/extref/@xlink:href)!='']">
                <dao xlink:title="{daodesc/list/item/name}">
                    <xsl:attribute name="xlink:href" select="ancestor::c[1]/otherfindaid/extref/@xlink:href"/>
                    <xsl:attribute name="xlink:role">
                        <xsl:apply-templates select="daodesc/list/item/genreform" mode="edm_type"/>
                    </xsl:attribute>
                </dao>
            </xsl:when>
        </xsl:choose>

    </xsl:template>

    <xsl:template match="node()" mode="edm_type">
        <xsl:choose>
            <xsl:when test=".='AUDIO'">SOUND</xsl:when>
            <xsl:when test=".='BILD'">IMAGE</xsl:when>
            <xsl:when test=".='TEXT'">TEXT</xsl:when>
            <xsl:when test=".='VIDEO'">VIDEO</xsl:when>
            <xsl:when test=".='SONSTIGES'">TEXT</xsl:when>
            <xsl:when test=".='VOLLTEXT'">TEXT</xsl:when>
            <xsl:otherwise>TEXT</xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template match="origination" mode="Findbuch">
        <origination>
            <xsl:call-template name="encoding_isadg"/>
            <xsl:apply-templates select="text()[normalize-space(.)!='']" mode="create_hyphen"/>
            <xsl:apply-templates select="child::node()[not(self::text()) and not(local-name(.)='lb')]" mode="Findbuch"/>
        </origination>
    </xsl:template>

    <xsl:template match="index[normalize-space(.)='']" mode="Findbuch"/>
    <xsl:template match="index[normalize-space(.)!='']" mode="Findbuch">
        <controlaccess>
            <xsl:apply-templates select="indexentry/child::node()" mode="no_gnd"/>
        </controlaccess>
    </xsl:template>

    <xsl:template match="node()" mode="no_gnd">
        <xsl:copy>
            <xsl:apply-templates select="text()[normalize-space(.)!='']" mode="create_hyphen"/>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="unittitle[ancestor::c[2][@level='series']]" mode="Findbuch">
        <unittitle>
            <xsl:call-template name="encoding_isadg"/>
            <xsl:choose>
                <xsl:when test="starts-with(.,ancestor::c[2][@level='series'])">
                    <xsl:apply-templates mode="Findbuch"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="concat(ancestor::c[2]/did/unittitle,': ')"/>
                    <xsl:apply-templates mode="Findbuch"/>
                </xsl:otherwise>
            </xsl:choose>
        </unittitle>
    </xsl:template>

    <!--corpname davorsetzen??-->
    <!--bestandsebene (exptr)-->
    <xsl:template match="unitid" mode="Findbuch">
        <unitid type="call number">
            <xsl:call-template name="encoding_isadg"/>
            <xsl:apply-templates select="text()" mode="Findbuch"/>
            <xsl:for-each select="ancestor::archdesc[1]/otherfindaid">
                <extptr xlink:href="{extref/@xlink:href}" xlink:title="{normalize-space(.)}"/>
            </xsl:for-each>
        </unitid>
    </xsl:template>

    <xsl:template match="unitid[ancestor::c[1][@level=('file','item')]][not(@type)]" mode="Findbuch">
        <unitid type="call number">
            <xsl:call-template name="encoding_isadg"/>
            <xsl:apply-templates select="text()" mode="Findbuch"/>
            <xsl:for-each select="ancestor::c[1]/otherfindaid">
                <extptr xlink:href="{extref/@xlink:href}" xlink:title="{normalize-space(.)}"/>
            </xsl:for-each>
        </unitid>
    </xsl:template>

    <xsl:template match="unitid[@type]" mode="Findbuch">
        <unitid type="former call number">
            <xsl:apply-templates select="text()" mode="Findbuch"/>
        </unitid>
    </xsl:template>

    <xsl:template match="emph|*:em" mode="Findbuch">
        <emph render="italic">
            <xsl:apply-templates mode="Findbuch"/>
        </emph>
    </xsl:template>
    <!--<xsl:template match="unitdate[position()&gt;1]"/>-->

    <xsl:template match="date[parent::p]" mode="Findbuch">
        <xsl:text> </xsl:text>
        <xsl:value-of select="if(normalize-space(.)!='') then . else @normal"/>
        <xsl:text> </xsl:text>
    </xsl:template>

    <xsl:template match="abstract" mode="Findbuch"/>
    <xsl:template match="abstract" mode="enthaeltvermerke_findbuch">
        <scopecontent>
            <xsl:if test="@type[not(starts-with(parent::node()/text()[1],.))]">
                <head>
                    <xsl:value-of select="@type"/>
                </head>
            </xsl:if>
            <p>
                <xsl:apply-templates mode="Findbuch"/>
            </p>
        </scopecontent>
    </xsl:template>

    <!--alle Elemente kopieren-->
    <xsl:template match="*" mode="Findbuch">
        <xsl:element name="{local-name(.)}">
            <xsl:apply-templates select="@*" mode="Findbuch"/>
            <xsl:call-template name="encoding_isadg"/>
            <xsl:apply-templates mode="Findbuch"/>
        </xsl:element>
    </xsl:template>

    <xsl:template name="encoding_isadg">
        <xsl:choose>
            <xsl:when test="local-name(.)='unitid' and not(@type)">
                <xsl:attribute name="encodinganalog" select="'3.1.1'"/>
            </xsl:when>
            <xsl:when test="local-name(.)='unittitle'">
                <xsl:if test="ancestor::c[1]/@level=('file','item')">
                    <xsl:attribute name="type" select="'title'"/>
                </xsl:if>
                <xsl:attribute name="encodinganalog" select="'3.1.2'"/>
            </xsl:when>
            <xsl:when test="local-name(.)='unitdate'">
                <xsl:if
                    test="@normal and matches(@normal,'^(\-?(0|1|2)([0-9]{3})(((01|02|03|04|05|06|07|08|09|10|11|12)((0[1-9])|((1|2)[0-9])|(3[0-1])))|\-((01|02|03|04|05|06|07|08|09|10|11|12)(\-((0[1-9])|((1|2)[0-9])|(3[0-1])))?))?)(/\-?(0|1|2)([0-9]{3})(((01|02|03|04|05|06|07|08|09|10|11|12)((0[1-9])|((1|2)[0-9])|(3[0-1])))|\-((01|02|03|04|05|06|07|08|09|10|11|12)(\-((0[1-9])|((1|2)[0-9])|(3[0-1])))?))?)?$')
                    ">
                    <xsl:attribute name="normal" select="@normal"/>
                </xsl:if>
                <xsl:attribute name="era" select="'ce'"/>
                <xsl:attribute name="calendar" select="'gregorian'"/>
                <xsl:attribute name="encodinganalog" select="'3.1.3'"/>
            </xsl:when>
            <xsl:when test="local-name(.)='c'">
                <xsl:attribute name="encodinganalog" select="'3.1.4'"/>
            </xsl:when>
            <xsl:when test="local-name(.)='physdesc'">
                <xsl:attribute name="encodinganalog" select="'3.1.5'"/>
            </xsl:when>
            <xsl:when test="local-name(.)='origination' and @label='pre'">
                <xsl:attribute name="label" select="'pre'"/>
                <xsl:attribute name="encodinganalog" select="'3.2.1'"/>
            </xsl:when>
            <xsl:when test="local-name(.)='origination' and (not(@label) or @label='Provenienz')">
                <xsl:attribute name="label" select="'final'"/>
                <xsl:attribute name="encodinganalog" select="'3.2.1'"/>
            </xsl:when>
            <xsl:when test="local-name(.)='origination' and @label">
                <xsl:attribute name="encodinganalog" select="'3.2.1'"/>
            </xsl:when>
            <xsl:when test="local-name(.)='accessrestrict'">
                <xsl:attribute name="encodinganalog" select="'3.4.1'"/>
            </xsl:when>
            <xsl:when test="local-name(.)='langmaterial'">
                <xsl:attribute name="encodinganalog" select="'3.4.3'"/>
            </xsl:when>
            <xsl:when test="local-name(.)='relatedmaterial'">
                <xsl:attribute name="encodinganalog" select="'3.5.3'"/>
            </xsl:when>
            <xsl:when test="local-name(.)=('odd','note')">
                <xsl:attribute name="encodinganalog" select="'3.6.1'"/>
            </xsl:when>
            <xsl:when test="local-name(.)='scopecontent'"/>
            <xsl:otherwise/>
        </xsl:choose>
    </xsl:template>

    <xsl:template match="otherfindaid" mode="Findbuch"/>

    <!-- Attribute rausfiltern, wo nicht vorgesehen-->
    <xsl:template match="@*" mode="Findbuch">
        <xsl:choose>
            <xsl:when test="local-name(.)='id'">
                <xsl:attribute name="id" select="mr:clear_id(.)"/>
            </xsl:when>
            <xsl:when test="local-name(.)='source'"/>
            <xsl:when test="local-name(.)='authfilenumber'">
                <xsl:attribute name="authfilenumber" select="concat('http://d-nb.info/gnd/',.)"/>
            </xsl:when>
            <xsl:when test="local-name(.)='normal' and local-name(parent::node())='genreform'"/>
            <xsl:when test="local-name(.)='level'">
                <xsl:apply-templates select="." mode="apex_levels"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:copy/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template match="@*" mode="apex_levels">
        <xsl:attribute name="level">
            <xsl:choose>
                <xsl:when test=".='collection'">
                    <xsl:value-of select="'fonds'"/>
                </xsl:when>
                <xsl:when test=".='class'">
                    <xsl:value-of select="'series'"/>
                </xsl:when>
                <xsl:when test=".='series'">
                    <xsl:value-of select="'series'"/>
                </xsl:when>
                <xsl:when test=".='file' and (parent::c/child::c[@level='class'])">
                    <xsl:value-of select="'series'"/>
                </xsl:when>
                <xsl:when test=".='file' and (parent::c/child::c[@level='file'] or parent::c/child::c/child::c)">
                    <xsl:value-of select="'subseries'"/>
                </xsl:when>
                <xsl:when test=".='item' and parent::c/child::c">
                    <xsl:value-of select="'subseries'"/>
                </xsl:when>

                <xsl:otherwise>
                    <xsl:value-of select="."/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:attribute>
    </xsl:template>

    <xsl:template match="text()[normalize-space(.)='']" mode="Findbuch"/>

    <xsl:template match="text()[normalize-space(.)!='']" mode="Findbuch">
        <xsl:variable name="cd_text" select="."/>
        <xsl:variable name="cd_text" select="replace($cd_text,'&lt;','(')"/>
        <xsl:variable name="cd_text" select="replace($cd_text,'&gt;',')')"/>

        <xsl:value-of select="$cd_text"/>

    </xsl:template>

    <xsl:template match="text()" mode="create_hyphen">
        <xsl:apply-templates select="self::node()" mode="Findbuch"/>
        <xsl:if test="position()!=last()">
            <xsl:text> - </xsl:text>
        </xsl:if>
    </xsl:template>

    <!--<xsl:template match="c[not(did/unittitle[normalize-space(.)!=''])]" mode="Tektonik"/>-->
    
    <xsl:template match="c" mode="Tektonik">
        <c>
            <xsl:choose>
                <xsl:when test="normalize-space(@id)=''"/>
                <xsl:when test="count(key('key_id',@id))&gt;1">
                    <xsl:attribute name="id" select="concat(mr:clear_id(@id),'_',generate-id(.))"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:attribute name="id" select="mr:clear_id(@id)"/>
                </xsl:otherwise>
            </xsl:choose>
            <xsl:apply-templates select="@*[local-name(.)!='id']" mode="Tektonik"/>
            <xsl:call-template name="encoding_isadg"/>
            <did>
                <xsl:apply-templates select="did/child::node()[local-name(.)=('unittitle','unitid','abstract')]" mode="Tektonik"/>
            </did>
            <xsl:apply-templates select="did/abstract" mode="enthaeltvermerke_tektonik"/>
            <xsl:if test="not(c) and @level='file'">
                <otherfindaid>
                    <p>
                        <extref xlink:href="{mr:clear_id(@id)}"/>
                    </p>
                </otherfindaid>
            </xsl:if>
            <xsl:apply-templates select="child::c" mode="Tektonik"/>
        </c>
        
    </xsl:template>
    
    <xsl:template match="unittitle[ancestor::c[1][@level='series']]" mode="Tektonik">
        <unittitle>
            <xsl:call-template name="encoding_isadg"/>
            <xsl:choose>
                <xsl:when test="starts-with(.,ancestor::c[1][@level='series'])">
                    <xsl:apply-templates mode="Tektonik"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="concat(ancestor::c[1]/did/unittitle,': ')"/><xsl:apply-templates mode="Tektonik"/>
                </xsl:otherwise>
            </xsl:choose>
        </unittitle>
    </xsl:template>
    
    <xsl:template match="unitid" mode="Tektonik">
        <unitid type="call number">
            <xsl:call-template name="encoding_isadg"/>
            <xsl:value-of select="text()"/>
        </unitid>
    </xsl:template>
    
    <xsl:template match="unitid[ancestor::c[1][@level=('file','item')]][not(@type)]" mode="Tektonik">
        <unitid type="call number">
            <xsl:call-template name="encoding_isadg"/>
            <xsl:value-of select="text()"/>
            <!--<xsl:for-each select="ancestor::c[1]/otherfindaid">
                <extptr xlink:href="{extref/@xlink:href}" xlink:title="{normalize-space(.)}"/>
            </xsl:for-each>-->
        </unitid>
    </xsl:template>
    
    <xsl:template match="emph|*:em" mode="Tektonik">
        <emph render="italic">
            <xsl:apply-templates mode="Tektonik"/>
        </emph>
    </xsl:template>
    
    <xsl:template match="abstract" mode="Tektonik"/>
    <xsl:template match="abstract" mode="enthaeltvermerke_tektonik">
        <scopecontent>
            <xsl:if test="@type">
                <head>
                    <xsl:value-of select="@type"/>
                </head>
            </xsl:if>
            <p>
                <xsl:apply-templates mode="Tektonik"/>
            </p>
        </scopecontent>
    </xsl:template>
    
    <!--alle Elemente kopieren-->
    <xsl:template match="*" mode="Tektonik">
        <xsl:element name="{local-name(.)}">
            <xsl:apply-templates select="@*" mode="Tektonik"/>
            <xsl:call-template name="encoding_isadg"/>
            <xsl:apply-templates mode="Tektonik"/>
        </xsl:element>
    </xsl:template>
    
    <xsl:template match="otherfindaid" mode="Tektonik"/>
    
    <!-- Attribute rausfiltern, wo nicht vorgesehen-->
    <xsl:template match="@*" mode="Tektonik">
        <xsl:choose>
            <xsl:when test="local-name(.)='id'">
                <xsl:attribute name="id" select="mr:clear_id(.)"/>
            </xsl:when>
            <xsl:when test="local-name(.)='level'">
                <xsl:apply-templates select="." mode="apex_levels"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:copy/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    <xsl:template match="text()[normalize-space(.)='']" mode="Tektonik"/>

</xsl:stylesheet>
