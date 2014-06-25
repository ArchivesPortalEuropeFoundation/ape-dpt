<?xml version="1.0" encoding="UTF-8"?>
<!--
        EAD default convertion into APE-EAD
        Modes:
            - lowest: file and item levels
            - intermediate: series and subseries levels
            - fonds: fonds level
            - copy: top elements + archdesc until the first c@level=fonds (not included)
            - other modes: specific for special purposes
-->
<xsl:stylesheet version="2.0" xmlns="urn:isbn:1-931666-22-9"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format"
                xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:none="none"
                xmlns:ape="http://www.archivesportaleurope.net/functions"
                xmlns:xlink="http://www.w3.org/1999/xlink"
                xsi:schemaLocation="urn:isbn:1-931666-22-9 http://www.loc.gov/ead/ead.xsd"
                xpath-default-namespace="urn:isbn:1-931666-22-9" exclude-result-prefixes="xsl fo xs none ape">

    <!--Will be used in the next version to use specialized xsl templates for partners-->
    <!--<xsl:include href="xsl/import.xsl"/>-->

    <xsl:param name="loclanguage" select="'xsl/system/languages.xml'"/>
    <xsl:variable name="langfile" select="document($loclanguage)"/>

    <xsl:param name="langusage" select="''"/>
    <xsl:param name="langmaterial" select="''"/>
    <xsl:param name="addressline" select="''"/>
    <xsl:param name="publisher" select="''"/>
    <xsl:param name="author" select="''"/>
    <xsl:param name="repository" select="''"/>
    <xsl:param name="prefercite" select="''"/>
    <xsl:param name="mainagencycode" select="''"/>
    <xsl:param name="countrycode" select="''"/>
    <xsl:param name="versionnb" select="''"/>
    <xsl:param name="useXSD10" select="'false'"/>
    <xsl:param name="eadidmissing" select="''"/>
    <xsl:param name="defaultRoleType" select="'UNSPECIFIED'"/>
    <xsl:param name="useDefaultRoleType" select="'false'"/>

    <xsl:param name="url" select="''"/>
    <xsl:param name="provider" select="''"/>

    <xsl:output indent="yes" method="xml"/>
    <xsl:strip-space elements="*"/>
    <!-- / -->

    <!--<xsl:include href="frontmatter.xsl"/>-->
    <!--<xsl:include href="levels.xsl"/>-->

    <xsl:template match="/">
        <xsl:apply-templates select="node()" mode="top"/>
    </xsl:template>

    <!--#all: copy fonds intermediate lowest nested-->
    <!-- Elements unknown -->
    <xsl:template match="*" name="excludeElement" mode="#all">
        <xsl:if test="ape:flagSet() = 'true'">
            <xsl:message>Element(s) excluded from the transformation:</xsl:message>
        </xsl:if>
        <xsl:variable name="excludedElement">
            <xsl:if test="name(../../../../../../../..) != ''">
                <xsl:value-of select="name(../../../../../../../..)"/>
                <xsl:if test="name(../../../../../../../..)='c'">@<xsl:value-of
                        select="../../../../../../../../@level"/>
                </xsl:if>
                <xsl:text>/</xsl:text>
            </xsl:if>
            <xsl:if test="name(../../../../../../..) != ''">
                <xsl:value-of select="name(../../../../../../..)"/>
                <xsl:if test="name(../../../../../../..)='c'">@<xsl:value-of
                        select="../../../../../../../@level"/>
                </xsl:if>
                <xsl:text>/</xsl:text>
            </xsl:if>
            <xsl:if test="name(../../../../../..) != ''">
                <xsl:value-of select="name(../../../../../..)"/>
                <xsl:if test="name(../../../../../..)='c'">@<xsl:value-of
                        select="../../../../../../@level"/>
                </xsl:if>
                <xsl:text>/</xsl:text>
            </xsl:if>
            <xsl:if test="name(../../../../..) != ''">
                <xsl:value-of select="name(../../../../..)"/>
                <xsl:if test="name(../../../../..)='c'">@<xsl:value-of
                        select="../../../../../@level"/>
                </xsl:if>
                <xsl:text>/</xsl:text>
            </xsl:if>
            <xsl:if test="name(../../../..) != ''">
                <xsl:value-of select="name(../../../..)"/>
                <xsl:if test="name(../../../..)='c'">@<xsl:value-of select="../../../../@level"
                    />
                </xsl:if>
                <xsl:text>/</xsl:text>
            </xsl:if>
            <xsl:if test="name(../../..) != ''">
                <xsl:value-of select="name(../../..)"/>
                <xsl:if test="name(../../..)='c'">@<xsl:value-of select="../../../@level"/></xsl:if>
                <xsl:text>/</xsl:text>
            </xsl:if>
            <xsl:value-of select="name(../..)"/>
            <xsl:if test="name(../..)='c'">@<xsl:value-of select="../../@level"/></xsl:if>
            <xsl:text>/</xsl:text>
            <xsl:value-of select="name(..)"/>
            <xsl:if test="name(..)='c'">@<xsl:value-of select="../@level"/></xsl:if>
            <xsl:text>/</xsl:text>
            <xsl:value-of select="name(.)"/>
            <xsl:if test="name(.)='c'">@<xsl:value-of select="@level"/></xsl:if>
        </xsl:variable>
        <xsl:message select="normalize-space($excludedElement)"/>
    </xsl:template>

    <!--
      copy all text nodes
      node() and text() have the same priority in XSLT 1.0 and 2.0
      to avoid "Recoverable error: XTRE0540: Ambiguous rule match for..."
      it is better to provide a priority
    -->
    <xsl:template match="text()" mode="#all" priority="2">
        <xsl:choose>
            <xsl:when test="parent::altformavail"/>
            <xsl:otherwise>
                <xsl:choose>
                    <xsl:when
                        test="(parent::accessrestrict or parent::bioghist) and normalize-space(.)">
                        <!--If file contains elements without p but directly text in it-->
                        <p>
                            <xsl:value-of select="."/>
                        </p>
                    </xsl:when>
                    <xsl:when test="contains(., '&#xa;')">
                        <xsl:value-of select="translate(normalize-space(.), '&#xa;', ' ')"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <!--xsl:if test="preceding-sibling::node()[1][self::node()]"><xsl:text> </xsl:text></xsl:if-->
                        <xsl:value-of select="."/>
                        <!--xsl:if test="following-sibling::node()[1][self::node()]"><xsl:text> </xsl:text></xsl:if-->
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <!-- lb is almost allowed everywhere -->
    <xsl:template match="lb" mode="#all">
        <xsl:choose>
            <xsl:when
                test="parent::unitdate or parent::emph or parent::origination or parent::physdesc or parent::entry or parent::item or parent::head or parent::physfacet or parent::bibref or parent::langusage or parent::title or parent::dimensions">
                <xsl:text> - </xsl:text>
            </xsl:when>
            <xsl:otherwise>
                <lb/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <!-- emph is only copied if it is of type render bold or italic - if not it just take the data inside the field and prints it -->
    <xsl:template match="emph" mode="#all">
        <xsl:choose>
            <xsl:when test="parent::bibref or parent::extref"/>
            <xsl:when
                test="parent::corpname or parent::origination or parent::physfacet or parent::persname or parent::head or parent::titleproper or parent::unitdate or parent::archref or parent::emph or parent::unittitle">
                <!--unittitle here is a hack - better fix it in the display xsl of portal/dashboard-->
                <xsl:value-of select="normalize-space(.)"/>
                <xsl:text> </xsl:text>
            </xsl:when>
            <xsl:when test="@render='bold' or @render='italic'">
                <emph>
                    <xsl:if test="@render != ''">
                        <xsl:attribute name="render" select="@render"/>
                    </xsl:if>
                    <xsl:value-of select="normalize-space(.)"/>
                </emph>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="normalize-space(.)"/>
                <xsl:text> </xsl:text>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <!-- abbr -->
    <xsl:template match="abbr" mode="#all">
        <abbr>
            <xsl:if test="@expan != ''">
                <xsl:attribute name="expan" select="@expan"/>
            </xsl:if>
            <xsl:value-of select="normalize-space(.)"/>
        </abbr>
    </xsl:template>

    <!-- expan -->
    <xsl:template match="expan" mode="#all">
        <xsl:choose>
            <xsl:when test="parent::physdesc"/>
            <xsl:otherwise>
                <expan>
                    <xsl:if test="@abbr != ''">
                        <xsl:attribute name="abbr" select="@abbr"/>
                    </xsl:if>
                    <xsl:value-of select="normalize-space(.)"/>
                </expan>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template match="ref" mode="#all">
        <xsl:value-of select="normalize-space(.)"/>
    </xsl:template>

    <xsl:template match="*[local-name() = 'eadgrp']" mode="top">
        <xsl:apply-templates mode="top"/>
    </xsl:template>
    <xsl:template match="*[local-name() = 'archdescgrp']" name="archdescgrp" mode="top">
        <xsl:apply-templates mode="top"/>
    </xsl:template>

    <!-- ead -->
    <xsl:template match="ead" name="ead" mode="top">
        <ead xmlns="urn:isbn:1-931666-22-9" xmlns:xlink="http://www.w3.org/1999/xlink"
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="urn:isbn:1-931666-22-9 http://www.archivesportaleurope.net/Portal/profiles/apeEAD_XSD1.0.xsd http://www.w3.org/1999/xlink http://www.loc.gov/standards/xlink/xlink.xsd"
             audience="external">
            <xsl:apply-templates select="node()" mode="copy"/>
        </ead>
    </xsl:template>

    <!-- eadheader -->
    <xsl:template match="eadheader" mode="copy">
        <eadheader countryencoding="iso3166-1" dateencoding="iso8601" langencoding="iso639-2b"
                   repositoryencoding="iso15511" scriptencoding="iso15924">
            <xsl:attribute name="relatedencoding" select="'MARC21'"/>
            <xsl:if test="not(eadid)">
                <xsl:call-template name="addEadid"/>
            </xsl:if>
            <xsl:apply-templates select="node()" mode="copy"/>
            <xsl:if test="not(revisiondesc) and normalize-space($versionnb)">
                <xsl:call-template name="revisiondesc_ape"/>
            </xsl:if>
        </eadheader>
    </xsl:template>

    <xsl:template name="revisiondesc_ape">
        <revisiondesc>
            <change>
                <date/>
                <item>Converted_apeEAD_version_<xsl:value-of select="$versionnb"/></item>
            </change>
        </revisiondesc>
    </xsl:template>

    <!-- revisiondesc -->
    <xsl:template match="revisiondesc" mode="copy">
        <xsl:choose>
            <xsl:when test="@audience='internal'">
                <xsl:call-template name="revisiondesc_ape"/>
            </xsl:when>
            <xsl:otherwise>
                <revisiondesc>
                    <xsl:apply-templates select="node()" mode="#current"/>
                    <xsl:if test="normalize-space($versionnb)">
                        <xsl:call-template name="revisiondesc_change"/>
                    </xsl:if>
                    <xsl:for-each select="list/item">
                        <change>
                            <date>
                                <xsl:if test="date">
                                    <xsl:attribute name="calendar" select="'gregorian'"/>
                                    <xsl:attribute name="era" select="'ce'"/>
                                    <xsl:for-each select="date">
                                        <xsl:call-template name="normalizeDate"/>
                                    </xsl:for-each>
                                    <xsl:value-of select="date/text()"/>
                                </xsl:if>
                            </date>
                            <item>
                                <xsl:apply-templates select="node()" mode="#current"/>
                            </item>
                        </change>
                    </xsl:for-each>
                </revisiondesc>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    <xsl:template match="revisiondesc/change" mode="copy">
        <change>
            <xsl:apply-templates select="node()" mode="#current"/>
        </change>
    </xsl:template>
    <xsl:template name="revisiondesc_change">
        <change>
            <xsl:call-template name="revisiondesc_item"/>
        </change>
    </xsl:template>
    <xsl:template match="revisiondesc/change/date" mode="copy">
        <date>
            <xsl:attribute name="calendar" select="'gregorian'"/>
            <xsl:attribute name="era" select="'ce'"/>
            <xsl:call-template name="normalizeDate"/>
            <xsl:apply-templates select="node()" mode="#current"/>
        </date>
    </xsl:template>
    <xsl:template match="revisiondesc/change/item" mode="copy">
        <item>
            <xsl:apply-templates select="node()" mode="#current"/>
        </item>
    </xsl:template>
    <xsl:template name="revisiondesc_item">
        <date/>
        <item>Converted_apeEAD_version_<xsl:value-of select="$versionnb"/></item>
    </xsl:template>
    <xsl:template match="revisiondesc/list" mode="copy"/>

    <!-- eadid -->
    <xsl:template match="eadid" name="addEadid" mode="copy">
        <eadid>
            <xsl:choose>
                <xsl:when test="@identifier">
                    <xsl:attribute name="identifier" select="@identifier"/>
                </xsl:when>
                <xsl:when test="@IDENTIFIER">
                    <xsl:attribute name="identifier" select="@IDENTIFIER"/>
                </xsl:when>
                <xsl:when
                    test="not(@identifier) and not(@IDENTIFIER) and text()[string-length(normalize-space(.)) ge 1] and normalize-space($mainagencycode)">
                    <xsl:attribute name="identifier">
                        <xsl:value-of select="$mainagencycode"/>
                        <xsl:text>_</xsl:text>
                        <xsl:value-of select="text()"/>
                    </xsl:attribute>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:attribute name="identifier">
                        <xsl:value-of select="$mainagencycode"/>
                        <xsl:text>_</xsl:text>
                        <xsl:value-of select="$eadidmissing"/>
                    </xsl:attribute>
                </xsl:otherwise>
            </xsl:choose>
            <xsl:if test="not(@countrycode) and normalize-space($countrycode)">
                <xsl:attribute name="countrycode" select="$countrycode"/>
            </xsl:if>
            <xsl:if test="@countrycode">
                <xsl:attribute name="countrycode" select="upper-case(@countrycode)"/>
            </xsl:if>
            <xsl:if test="not(@mainagencycode) and normalize-space($mainagencycode)">
                <xsl:attribute name="mainagencycode" select="$mainagencycode"/>
            </xsl:if>
            <xsl:if test="@mainagencycode">
                <xsl:variable name="myCode">
                    <xsl:value-of select="ape:normalizeDate(@mainagencycode, 'mainagencycode')"/>
                </xsl:variable>
                <xsl:choose>
                    <xsl:when test="normalize-space($myCode)">
                        <xsl:attribute name="mainagencycode">
                            <xsl:value-of select="$myCode"/>
                        </xsl:attribute>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:if test="@countrycode">
                            <xsl:variable name="myCode_2">
                                <xsl:value-of
                                    select="ape:normalizeDate(concat(concat(@countrycode,'-'),@mainagencycode), 'mainagencycode')"
                                />
                            </xsl:variable>
                            <xsl:choose>
                                <xsl:when test="normalize-space($myCode_2)">
                                    <xsl:attribute name="mainagencycode">
                                        <xsl:value-of select="$myCode_2"/>
                                    </xsl:attribute>
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:if test="normalize-space($mainagencycode)">
                                        <xsl:attribute name="mainagencycode">
                                            <xsl:value-of select="$mainagencycode"/>
                                        </xsl:attribute>
                                    </xsl:if>
                                </xsl:otherwise>
                            </xsl:choose>
                        </xsl:if>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:if>
            <xsl:if test="not(@url) and normalize-space($url)">
                <xsl:attribute name="url" select="ape:checkLink($url)"/>
            </xsl:if>
            <xsl:if test="@url">
                <xsl:attribute name="url" select="ape:checkLink(@url)"/>
            </xsl:if>
            <xsl:choose>
                <xsl:when test="not(text())">
                    <xsl:value-of select="$eadidmissing"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:apply-templates select="text()" mode="copy"/>
                </xsl:otherwise>
            </xsl:choose>
        </eadid>
    </xsl:template>

    <!-- filedesc -->
    <xsl:template match="filedesc" mode="copy">
        <filedesc>
            <xsl:apply-templates select="node()" mode="copy"/>
            <xsl:if test="not(titlestmt)">
                <xsl:call-template name="titlestmt"/>
            </xsl:if>
            <xsl:if
                test="not(publicationstmt) and (normalize-space($addressline) or normalize-space($publisher))">
                <xsl:call-template name="publicationstmt"/>
            </xsl:if>
        </filedesc>
    </xsl:template>

    <!-- publicationstmt -->
    <xsl:template match="publicationstmt" name="publicationstmt" mode="copy">
        <publicationstmt>
            <xsl:apply-templates select="node() except titlestmt" mode="copy"/>
            <xsl:if test="not(address)">
                <xsl:call-template name="address"/>
            </xsl:if>
            <xsl:if test="not(publisher)">
                <xsl:call-template name="publisher"/>
            </xsl:if>
        </publicationstmt>
    </xsl:template>

    <!-- publicationstmt/p => publisher for Poland -->
    <xsl:template match="publicationstmt/p" mode="copy">
        <xsl:choose>
            <xsl:when test="//eadid/@countrycode = 'PL' or //eadid/@countrycode = 'pl'">
                <publisher encodinganalog="260$b">
                    <xsl:apply-templates select="node()" mode="#current"/>
                </publisher>
            </xsl:when>
            <xsl:otherwise>
                <xsl:call-template name="excludeElement"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <!-- titlestmt -->
    <xsl:template match="titlestmt" name="titlestmt" mode="copy">
        <titlestmt>
            <xsl:apply-templates select="node()" mode="copy"/>
            <xsl:if test="not(author)">
                <xsl:call-template name="author"/>
            </xsl:if>
        </titlestmt>
    </xsl:template>

    <!-- titlestmt/subtitle -->
    <xsl:template match="titlestmt/subtitle" mode="copy">
        <subtitle>
            <xsl:apply-templates select="node()" mode="copy"/>
        </subtitle>
    </xsl:template>

    <!-- seriesstmt -->
    <xsl:template match="seriesstmt" mode="copy">
        <seriesstmt>
            <xsl:apply-templates select="titleproper" mode="copy"/>
            <xsl:if test="not(titleproper)">
                <titleproper/>
            </xsl:if>
        </seriesstmt>
    </xsl:template>

    <!-- filedesc/titlestmt/author -->
    <xsl:template match="filedesc/titlestmt/author" name="author" mode="copy">
        <xsl:choose>
            <xsl:when test="text()[string-length(normalize-space(.)) ge 1]">
                <author>
                    <xsl:attribute name="encodinganalog" select="'245$c'"/>
                    <xsl:value-of select="text()"/>
                </author>
            </xsl:when>
            <xsl:when test="normalize-space($author)">
                <author>
                    <xsl:attribute name="encodinganalog" select="'245$c'"/>
                    <xsl:value-of select="$author"/>
                </author>
            </xsl:when>
        </xsl:choose>
    </xsl:template>

    <!-- filedesc/publicationstmt/publisher -->
    <xsl:template match="filedesc/publicationstmt/publisher" name="publisher" mode="copy">
        <xsl:choose>
            <xsl:when test="text()[string-length(normalize-space(.)) ge 1]">
                <publisher>
                    <xsl:if test="@encodinganalog=''">
                        <xsl:attribute name="encodinganalog" select="'260$b'"/>
                    </xsl:if>
                    <xsl:if test="@encodinganalog!=''">
                        <xsl:attribute name="encodinganalog" select="@encodinganalog"/>
                    </xsl:if>
                    <xsl:value-of select="text()"/>
                </publisher>
            </xsl:when>
            <xsl:when test="normalize-space($publisher)">
                <publisher>
                    <xsl:attribute name="encodinganalog" select="'260$b'"/>
                    <xsl:value-of select="$publisher"/>
                </publisher>
            </xsl:when>
            <xsl:otherwise>
                <publisher/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <!-- filedesc/publicationstmt/date -->
    <xsl:template match="filedesc/publicationstmt/date" mode="copy">
        <date encodinganalog="260$c">
            <xsl:attribute name="calendar" select="'gregorian'"/>
            <xsl:attribute name="era" select="'ce'"/>
            <xsl:if test="@encodinganalog!=''">
                <xsl:attribute name="encodinganalog" select="@encodinganalog"/>
            </xsl:if>
            <xsl:call-template name="normalizeDate"/>
            <xsl:value-of select="."/>
        </date>
    </xsl:template>

    <!-- filedesc/publicationstmt/address -->
    <xsl:template match="filedesc/publicationstmt/address" name="address" mode="copy">
        <xsl:choose>
            <xsl:when test="./addressline/text()[string-length(normalize-space(.)) ge 1]">
                <address>
                    <xsl:for-each select="./addressline">
                        <addressline>
                            <xsl:value-of select="./text()"/>
                        </addressline>
                    </xsl:for-each>
                </address>
            </xsl:when>
            <xsl:when test="normalize-space($addressline)">
                <address>
                    <addressline>
                        <xsl:value-of select="$addressline"/>
                    </addressline>
                </address>
            </xsl:when>
        </xsl:choose>
    </xsl:template>

    <!-- titleproper -->
    <xsl:template match="titleproper" mode="copy">
        <titleproper encodinganalog="245">
            <xsl:if test="@type!=''">
                <xsl:attribute name="type" select="@type"/>
            </xsl:if>
            <xsl:if test="@encodinganalog !=''">
                <xsl:attribute name="encodinganalog" select="@encodinganalog "/>
            </xsl:if>
            <xsl:apply-templates select="node()" mode="copy"/>
            <!-- For Portugal-->
            <xsl:if test="normalize-space(.) = ''">
                <xsl:value-of select="//archdesc/did/unittitle/text()"/>
            </xsl:if>
        </titleproper>
    </xsl:template>

    <!-- profiledesc -->
    <xsl:template match="profiledesc" mode="copy">
        <profiledesc>
            <xsl:apply-templates select="node()" mode="copy"/>
            <xsl:if test="not(langusage)">
                <xsl:call-template name="langusage"/>
            </xsl:if>
        </profiledesc>
    </xsl:template>

    <!-- profiledesc/creation -->
    <xsl:template match="profiledesc/creation" mode="copy">
        <xsl:if test="not(@audience='internal')">
            <creation>
                <xsl:for-each select="descendant::text()">
                    <xsl:value-of select="concat(normalize-space(.), ' ')"/>
                </xsl:for-each>
                <!--<xsl:for-each select="date[1]">-->
                <!--<date>-->
                <!--<xsl:attribute name="calendar" select="'gregorian'"/>-->
                <!--<xsl:attribute name="era" select="'ce'"/>-->
                <!--<xsl:call-template name="normalizeDate"/>-->
                <!--<xsl:value-of select="normalize-space(.)"/>-->
                <!--</date>-->
                <!--</xsl:for-each>-->
            </creation>
        </xsl:if>
    </xsl:template>

    <!-- profiledesc/langusage -->
    <xsl:template match="profiledesc/langusage" mode="copy">
        <langusage>
            <xsl:apply-templates select="node()" mode="copy"/>
        </langusage>
    </xsl:template>

    <!-- profiledesc/langusage/language -->
    <!-- todo: This will have to be modified into a nicer function -->
    <xsl:template match="profiledesc/langusage/language" mode="copy">
        <xsl:variable name="langusagetemp">
            <xsl:choose>
                <xsl:when test="normalize-space($langusage)">
                    <xsl:for-each select="tokenize(normalize-space($langusage), ';')">
                        <xsl:if
                            test="current()[normalize-space()] and exists($langfile//none:language[@code=normalize-space(current())])">
                            <language encodinganalog="041">
                                <xsl:attribute name="langcode">
                                    <xsl:value-of
                                        select="$langfile//none:language[@code=normalize-space(current())]/none:langcode"
                                    />
                                </xsl:attribute>
                                <xsl:attribute name="scriptcode">
                                    <xsl:value-of
                                        select="$langfile//none:language[@code=normalize-space(current())]/none:scriptcode"
                                    />
                                </xsl:attribute>
                                <xsl:value-of
                                    select="$langfile//none:language[@code=normalize-space(current())]/none:langname[@language='0x409']"
                                />
                            </language>
                        </xsl:if>
                    </xsl:for-each>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:choose>
                        <xsl:when
                            test="exists($langfile//none:language[@code=normalize-space(current()/@langcode)])">
                            <language encodinganalog="041">
                                <xsl:attribute name="langcode">
                                    <xsl:value-of
                                        select="$langfile//none:language[@code=normalize-space(current()/@langcode)]/none:langcode"
                                    />
                                </xsl:attribute>
                                <xsl:attribute name="scriptcode">
                                    <xsl:value-of
                                        select="$langfile//none:language[@code=normalize-space(current()/@langcode)]/none:scriptcode"
                                    />
                                </xsl:attribute>
                                <xsl:apply-templates select="node()" mode="#current"/>
                            </language>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:apply-templates select="node()" mode="#current"/>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <xsl:choose>
            <xsl:when test="normalize-space($langusagetemp)">
                <xsl:copy-of select="$langusagetemp"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:apply-templates select="node()" mode="copy"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template match="profiledesc/descrules" mode="copy">
        <xsl:if test="not(@audience='internal')">
            <descrules>
                <xsl:if test="@audience">
                    <xsl:attribute name="audience" select="@audience"/>
                </xsl:if>
                <xsl:apply-templates select="node()" mode="#current"/>
            </descrules>
        </xsl:if>
    </xsl:template>

    <xsl:template match="descrules//*" mode="copy">
        <xsl:apply-templates select="node()" mode="#current"/>
    </xsl:template>

    <!-- copy fonds intermediate lowest: langusage -->
    <!-- todo: This will be rewritten has well -->
    <xsl:template match="langusage" name="langusage" mode="copy fonds intermediate lowest">
        <xsl:variable name="langusagetemp">
            <xsl:if test="normalize-space($langusage)">
                <langusage>
                    <xsl:for-each select="tokenize(normalize-space($langusage), ';')">
                        <xsl:if
                            test="current()[normalize-space()] and exists($langfile//none:language[@code=normalize-space(current())])">
                            <language encodinganalog="041">
                                <xsl:attribute name="langcode">
                                    <xsl:value-of
                                        select="$langfile//none:language[@code=normalize-space(current())]/none:langcode"
                                    />
                                </xsl:attribute>
                                <xsl:attribute name="scriptcode">
                                    <xsl:value-of
                                        select="$langfile//none:language[@code=normalize-space(current())]/none:scriptcode"
                                    />
                                </xsl:attribute>
                                <xsl:value-of
                                    select="$langfile//none:language[@code=normalize-space(current())]/none:langname[@language='0x409']"
                                />
                            </language>
                        </xsl:if>
                    </xsl:for-each>
                </langusage>
            </xsl:if>
        </xsl:variable>
        <xsl:if test="normalize-space($langusagetemp)">
            <xsl:copy-of select="$langusagetemp"/>
        </xsl:if>
    </xsl:template>

    <xsl:template match="frontmatter" mode="copy">
        <xsl:if test="not($countrycode='FR')">
            <xsl:call-template name="excludeElement"/>
        </xsl:if>
    </xsl:template>

    <!-- archdesc -->
    <xsl:template match="archdesc" mode="copy">
        <archdesc level="fonds" type="inventory" encodinganalog="3.1.4" relatedencoding="ISAD(G)v2">
            <xsl:apply-templates select="did" mode="copy"/>
            <xsl:if test="not(prefercite) and normalize-space($prefercite)">
                <prefercite>
                    <p>
                        <xsl:value-of select="$prefercite"/>
                    </p>
                </prefercite>
            </xsl:if>
            <xsl:apply-templates select="node() except (did|dsc)" mode="copy"/>
            <xsl:if
                test="descendant::geogname[parent::item|parent::entry|parent::p|parent::unittitle] or descendant::subject[parent::item|parent::entry|parent::p|parent::unittitle] or descendant::famname[parent::item|parent::entry|parent::p|parent::unittitle] or descendant::persname[parent::item|parent::entry|parent::p|parent::unittitle] or descendant::corpname[parent::item|parent::entry|parent::p|parent::unittitle] or descendant::occupation[parent::item|parent::entry|parent::p|parent::unittitle] or descendant::genreform[parent::item|parent::entry|parent::p|parent::unittitle] or descendant::function[parent::item|parent::entry|parent::p|parent::unittitle] or descendant::title[parent::item|parent::entry|parent::p|parent::unittitle] or descendant::name[parent::item|parent::entry|parent::p|parent::unittitle]">
                <xsl:call-template name="createControlaccess">
                    <xsl:with-param name="context" select="*[not(c)]"/>
                </xsl:call-template>
            </xsl:if>
            <xsl:apply-templates select="dsc" mode="copy"/>
        </archdesc>
    </xsl:template>

    <!-- All p/persname, p/geogname and p/date are just discarded, we take the data but not the elements -->
    <xsl:template
        match="p/persname | p/geogname | p/corpname | p/title | p/date | p/name | p/subject | p/famname | p/function | p/genreform | p/occupation"
        mode="#all">
        <xsl:value-of select="normalize-space(.)"/>
    </xsl:template>

    <xsl:template match="p/extref | p/extptr" mode="#all">
        <xsl:text> </xsl:text>
        <extref>
            <xsl:if test="@href">
                <xsl:attribute name="xlink:href" select="ape:checkLink(@href)"/>
            </xsl:if>
            <xsl:if test="@*:href">
                <xsl:attribute name="xlink:href" select="ape:checkLink(@*:href)"/>
            </xsl:if>
            <xsl:if test="@title">
                <xsl:attribute name="xlink:title" select="@title"/>
            </xsl:if>
            <xsl:if test="@*:title">
                <xsl:attribute name="xlink:title" select="@*:title"/>
            </xsl:if>
            <!--xsl:value-of select="normalize-space(.)"/-->
            <xsl:apply-templates select="node()" mode="#current"/>
        </extref>
        <xsl:text> </xsl:text>
    </xsl:template>

    <xsl:template match="p/extref/corpname" mode="#all">
        <xsl:apply-templates select="node" mode="#current"/>
    </xsl:template>

    <xsl:template match="p/extref/p" mode="#all">
        <xsl:apply-templates select="node" mode="#current"/>
    </xsl:template>


    <!-- Here begin something for NL-->
    <!--todo: Ok, this WILL need to be changed, here we pass other info that are not sent to the user but instead are just discarded-->
    <!-- descgrp -->

    <xsl:template match="archdesc/descgrp[not(@type='appendices')]" mode="copy">
        <xsl:apply-templates
            select="bibliography | bioghist | custodhist | custodhist/acqinfo | accruals | appraisal | arrangement | originalsloc | processinfo | scopecontent | accessrestrict | userestrict | otherfindaid | prefercite | separatedmaterial | odd | controlaccess | phystech | relatedmaterial | index"
            mode="copy"/>
    </xsl:template>

    <!-- descgrp/bioghist -->
    <!--xsl:template match="archdesc/descgrp/bioghist" mode="copy">
        <bioghist encodinganalog="3.2.2">
            <xsl:apply-templates select="* except bioghist" mode="copy"/>
            <xsl:apply-templates select="bioghist" mode="nested_bioghist"/>
        </bioghist>
    </xsl:template-->

    <!-- descgrp/bioghist/bioghist (nested into bioghist) -->
    <!--xsl:template match="bioghist" mode="nested_bioghist">
        <xsl:apply-templates mode="nested_bioghist"/>
    </xsl:template-->

    <!-- descgrp/bioghist/bioghist/head (nested into bioghist) -->
    <!--xsl:template match="head" mode="nested_bioghist">
        <p>
            <emph render="bold">
                <xsl:value-of select="."/>
            </emph>
        </p>
    </xsl:template-->

    <!-- descgrp/bioghist/bioghist/p (nested into bioghist) -->
    <!--xsl:template match="p" mode="nested_bioghist">
        <p>
            <xsl:value-of select="."/>
        </p>
        <xsl:apply-templates select="persname | geogname" mode="nested_bioghist"/>
    </xsl:template-->

    <!-- descgrp/custohist -->
    <xsl:template match="archdesc/descgrp/custodhist" mode="copy">
        <custodhist encodinganalog="3.2.3">
            <!-- "acqinfo" goes one level uo to the same level as "custodhist" -->
            <xsl:apply-templates select="node()[not(name()='acqinfo' or name()='custodhist')]"
                                 mode="copy"/>
            <xsl:apply-templates select="custodhist" mode="nested"/>
            <xsl:if test="not(p)">
                <p/>
            </xsl:if>
        </custodhist>
    </xsl:template>

    <!-- descgrp/accruals -->
    <!--xsl:template match="archdesc/descgrp/accruals" mode="copy">
        <accruals encodinganalog="3.3.3">
            <xsl:apply-templates mode="copy"/>
        </accruals>
    </xsl:template-->

    <!-- descgrp/odd -->
    <xsl:template match="archdesc/descgrp/odd" mode="copy">
        <odd encodinganalog="3.6.1">
            <xsl:apply-templates select="node() except odd" mode="copy"/>
            <xsl:apply-templates select="odd/*" mode="nested"/>
        </odd>
    </xsl:template>
    <xsl:template match="odd" mode="nested">
        <xsl:apply-templates select="node()" mode="nested"/>
    </xsl:template>

    <!-- descgrp/appraisal -->
    <xsl:template match="archdesc/descgrp/appraisal" mode="copy">
        <appraisal encodinganalog="3.3.2">
            <xsl:apply-templates select="node() except appraisal" mode="copy"/>
            <xsl:apply-templates select="appraisal/*" mode="nested"/>
        </appraisal>
    </xsl:template>

    <!-- descgrp/otherfindaid -->
    <xsl:template match="archdesc/descgrp/otherfindaid" mode="copy">
        <otherfindaid encodinganalog="3.4.5">
            <xsl:apply-templates mode="copy"/>
        </otherfindaid>
    </xsl:template>

    <!-- descgrp[@type='allied_materials'][altformavail] -->
    <xsl:template match="archdesc/descgrp[@type='allied_materials'][altformavail]" priority="10"
                  mode="copy">
        <altformavail encodinganalog="3.5.2">
            <xsl:apply-templates select="altformavail/node() except altformavail/altformavail"
                                 mode="copy"/>
            <xsl:apply-templates select="altformavail/altformavail/*" mode="nested"/>
        </altformavail>
    </xsl:template>
    <!--<xsl:template match="archdesc/descgrp[@type='allied_materials'][originalsloc]" priority="10" mode="copy">-->
    <!--<originalsloc encodinganalog="3.5.1">-->
    <!--<xsl:apply-templates select="originalsloc/node() except originalsloc/originalsloc" mode="copy"/>-->
    <!--<xsl:apply-templates select="originalsloc/originalsloc/*" mode="nested"/>-->
    <!--</originalsloc>-->
    <!--</xsl:template>-->
    <!--<xsl:template match="archdesc/descgrp[@type='allied_materials'][separatedmaterial]" priority="10" mode="copy">-->
    <!--<originalsloc encodinganalog="3.5.1">-->
    <!--<xsl:apply-templates select="originalsloc/node() except originalsloc/originalsloc" mode="copy"/>-->
    <!--<xsl:apply-templates select="originalsloc/originalsloc/*" mode="nested"/>-->
    <!--</originalsloc>-->
    <!--</xsl:template>-->

    <!-- descgrp/relatedmaterial -->
    <xsl:template match="archdesc/descgrp/relatedmaterial" mode="copy">
        <relatedmaterial encodinganalog="3.5.3">
            <xsl:apply-templates mode="copy"/>
        </relatedmaterial>
    </xsl:template>

    <!--descgrp/separatedmaterial -->
    <xsl:template match="archdesc/descgrp/separatedmaterial" mode="copy">
        <separatedmaterial encodinganalog="3.5.3">
            <xsl:apply-templates mode="copy"/>
        </separatedmaterial>
    </xsl:template>

    <!--descgrp/bibliography -->
    <xsl:template match="archdesc/descgrp/bibliography" mode="copy">
        <bibliography encodinganalog="3.5.4">
            <xsl:apply-templates mode="copy"/>
        </bibliography>
    </xsl:template>

    <!-- descgrp[@type='appendices'] -->
    <xsl:template match="archdesc/descgrp[@type='appendices']" mode="copy">
        <xsl:apply-templates select="fileplan | index | odd" mode="copy"/>
    </xsl:template>

    <xsl:template
        match="archdesc/descgrp[@type='content_and_structure']/controlaccess[@audience='internal']"
        mode="copy"/>

    <xsl:template match="archdesc/descgrp[@type='access_and_use']/phystech">
        <xsl:apply-templates select="phystech" mode="copy"/>
    </xsl:template>

    <!--End of the NL descgrp-->

    <!-- archdesc/did -->
    <xsl:template match="archdesc/did" mode="copy">
        <did>
            <xsl:apply-templates select="node() except abstract" mode="copy"/>
            <xsl:if test="not(repository) and normalize-space($repository)">
                <xsl:call-template name="repository"/>
            </xsl:if>
            <xsl:if test="not(langmaterial) and $langmaterial">
                <xsl:call-template name="langmaterial"/>
            </xsl:if>
            <xsl:for-each select="following-sibling::note">
                <xsl:call-template name="note"/>
            </xsl:for-each>
        </did>
        <xsl:if test="//eadid/@countrycode='fr' and /ead/frontmatter">
            <xsl:call-template name="frontmatter2scopecontent"/>
        </xsl:if>
        <!--<xsl:choose>
            <xsl:when test="abstract/@encodinganalog='Zusammenfassung'">
                <xsl:apply-templates select="abstract[@encodinganalog='Zusammenfassung']" mode="abstractGer"/>
            </xsl:when>
            <xsl:otherwise>
                --><xsl:apply-templates select="abstract" mode="#current"/><!--
            </xsl:otherwise>
        </xsl:choose>-->
    </xsl:template>

    <xsl:template name="frontmatter2scopecontent">
        <xsl:apply-templates select="/ead/frontmatter" mode="frontmatter"/>
    </xsl:template>

    <xsl:template match="frontmatter" mode="frontmatter">
        <scopecontent encodinganalog="summary">
            <xsl:apply-templates mode="frontmatter"/>
        </scopecontent>
    </xsl:template>

    <xsl:template match="div" mode="frontmatter">
        <p>
            <xsl:apply-templates mode="frontmatter"/>
        </p>
    </xsl:template>

    <xsl:template match="div/head" mode="frontmatter">
        <xsl:apply-templates mode="frontmatter"/>
    </xsl:template>

    <xsl:template match="div/p | div/p/note/p" mode="frontmatter">
        <xsl:apply-templates mode="frontmatter"/>
    </xsl:template>

    <xsl:template match="div/p/note" mode="frontmatter">
        <xsl:apply-templates mode="frontmatter"/>
    </xsl:template>

    <xsl:template match="div/p/list | div/p/list/item" mode="frontmatter">
        <xsl:apply-templates mode="frontmatter"/>
    </xsl:template>

    <xsl:template match="archdesc/note" mode="copy"/>

    <!-- copy fonds intermediate lowest: did/note -->
    <xsl:template match="did/note" name="note" mode="copy fonds intermediate lowest">
        <xsl:if test="count(child::*) != 0 or normalize-space(text()) != ''">
            <note>
                <xsl:if test="@encodinganalog !=''">
                    <xsl:attribute name="encodinganalog" select="@encodinganalog"/>
                </xsl:if>
                <xsl:if test="@label!=''">
                    <xsl:attribute name="label" select="@label"/>
                </xsl:if>
                <xsl:choose>
                    <xsl:when test="count(child::text()) &gt; 0">
                        <xsl:call-template name="note_no_p"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:apply-templates mode="#current"/>
                    </xsl:otherwise>
                </xsl:choose>
            </note>
        </xsl:if>
    </xsl:template>

    <!-- copy fonds intermediate lowest: note/p -->
    <xsl:template match="note/p" name="note_no_p" mode="copy fonds intermediate lowest">
        <p>
            <xsl:apply-templates select="node()" mode="#current"/>
        </p>
    </xsl:template>


    <!-- copy: archdesc/originalsloc -->
    <xsl:template match="archdesc/originalsloc" mode="copy">
        <originalsloc encodinganalog="3.5.1">
            <xsl:apply-templates select="node()" mode="#current"/>
        </originalsloc>
    </xsl:template>

    <!-- archdesc/did/unittitle -->
    <xsl:template match="archdesc/did/unittitle" mode="copy">
        <unittitle encodinganalog="3.1.2">
            <xsl:if test="@type">
                <xsl:attribute name="type" select="@type"/>
            </xsl:if>
            <xsl:value-of select="."/>
        </unittitle>
    </xsl:template>

    <!-- archdesc/did/unitid -->
    <xsl:template match="archdesc/did/unitid" mode="copy">
        <xsl:choose>
            <xsl:when test="//eadid/@countrycode='NL'">
                <unitid encodinganalog="3.1.1" type="call number">
                    <xsl:value-of select="//eadid/text()"/>
                </unitid>
            </xsl:when>
            <xsl:when test="@countrycode and @repositorycode">
                <unitid encodinganalog="3.1.1" type="call number">
                    <xsl:value-of
                        select="concat(concat(concat(concat(@countrycode,'/'),@repositorycode), '/'), .)"
                    />
                </unitid>
            </xsl:when>
            <xsl:otherwise>
                <unitid encodinganalog="3.1.1" type="call number">
                    <xsl:if test="extptr">
                        <xsl:apply-templates select="extptr" mode="#current"/>
                    </xsl:if>
                    <xsl:value-of select="."/>
                </unitid>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <!-- archdesc/did/head -->
    <xsl:template match="archdesc/did/head" mode="copy">
        <!--<xsl:if test="$provider != 'NL'">-->
        <xsl:call-template name="excludeElement"/>
        <!--</xsl:if>-->
    </xsl:template>

    <!-- archdesc/did/unitdate -->
    <xsl:template match="archdesc/did/unitdate" mode="copy">
        <unitdate>
            <xsl:attribute name="calendar" select="'gregorian'"/>
            <xsl:attribute name="era" select="'ce'"/>
            <xsl:call-template name="normalizeDate"/>
            <xsl:attribute name="encodinganalog" select="'3.1.3'"/>
            <xsl:apply-templates select="node()"/>
        </unitdate>
    </xsl:template>

    <!-- archdesc/did/abstract -->
    <!-- todo: Here modifications has to be done, we only accept @encodinganalog=('summary'|'preface') -->
    <xsl:template match="archdesc/did/abstract" mode="copy">
        <scopecontent encodinganalog="summary">
            <xsl:if test="@type">
                <head>
                    <xsl:value-of select="@type"/>
                </head>
            </xsl:if>
            <p>
                <xsl:apply-templates select="node()" mode="copy"/>
            </p>
        </scopecontent>
    </xsl:template>

    <!-- Special archdesc/did/abstract templates for German software -->
    <xsl:template match="archdesc/did/abstract[@encodinganalog='Kopfzeile']" mode="copy">
        <scopecontent encodinganalog="summary">
            <head>
                <xsl:apply-templates select="node()" mode="copy"/>
            </head>
            <xsl:if test="following-sibling::abstract[@encodinganalog='Zusammenfassung' and position()=1]">
                <p>
                    <xsl:apply-templates select="following-sibling::abstract[@encodinganalog='Zusammenfassung' and position()=1]/node()" mode="copy"/>
                </p>
            </xsl:if>
        </scopecontent>
    </xsl:template>

    <xsl:template match="archdesc/did/abstract[@encodinganalog='Zusammenfassung']" mode="copy">
            <!--<p>
                <xsl:apply-templates select="node()" mode="copy"/>
            </p>-->
    </xsl:template>

    <!-- copy fonds intermediate lowest: did/origination -->
    <!-- todo: We only accept @encoding=('pre'|'final'|'organisational unit') -->
    <xsl:template match="did/origination" mode="copy fonds intermediate lowest">
        <origination>
            <xsl:attribute name="encodinganalog" select="'3.2.1'"/>
            <xsl:if test="@label!=''">
                <xsl:choose>
                    <xsl:when test="@label = 'Organisationseinheit'">
                        <xsl:attribute name="label" select="'organisational unit'"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:attribute name="label" select="@label"/>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:if>
            <xsl:apply-templates select="node()" mode="#current"/>
        </origination>
    </xsl:template>

    <!-- copy fonds intermediate lowest did/origination/name | did/origination/persname | did/origination/corpname | did/origination/famname -->
    <xsl:template
        match="did/origination/name | did/origination/persname | did/origination/corpname | did/origination/famname"
        mode="copy fonds intermediate lowest">
        <xsl:element name="{local-name()}" namespace="urn:isbn:1-931666-22-9">
            <xsl:if test="@authfilenumber">
                <xsl:attribute name="authfilenumber" select="@authfilenumber"/>
            </xsl:if>
            <xsl:apply-templates select="node()" mode="#current"/>
        </xsl:element>
    </xsl:template>

    <!-- langmaterial -->
    <xsl:template match="langmaterial" name="langmaterial" mode="copy fonds intermediate lowest">
        <langmaterial encodinganalog="3.4.3">
            <xsl:apply-templates select="node()" mode="#current"/>
        </langmaterial>
    </xsl:template>

    <!-- copy fonds intermediate lowest: langmaterial -->
    <!--todo: This also will be modified to be nicer-->
    <xsl:template match="langmaterial/language" mode="copy fonds intermediate lowest">
        <xsl:variable name="langmaterialtemp">
            <xsl:choose>
                <xsl:when test="normalize-space($langmaterial)">
                    <xsl:for-each select="tokenize(normalize-space($langmaterial), ';')">
                        <xsl:if
                            test="current()[normalize-space()] and exists($langfile//none:language[@code=normalize-space(current())])">
                            <language>
                                <xsl:attribute name="langcode">
                                    <xsl:value-of
                                        select="$langfile//none:language[@code=normalize-space(current())]/none:langcode"
                                    />
                                </xsl:attribute>
                                <xsl:attribute name="scriptcode">
                                    <xsl:value-of
                                        select="$langfile//none:language[@code=normalize-space(current())]/none:scriptcode"
                                    />
                                </xsl:attribute>
                                <xsl:value-of
                                    select="$langfile//none:language[@code=normalize-space(current())]/none:langname[@language='0x409']"
                                />
                            </language>
                        </xsl:if>
                    </xsl:for-each>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:choose>
                        <xsl:when
                            test="exists($langfile//none:language[@code=normalize-space(current()/@langcode)])">
                            <language>
                                <xsl:attribute name="langcode">
                                    <xsl:value-of
                                        select="$langfile//none:language[@code=normalize-space(current()/@langcode)]/none:langcode"
                                    />
                                </xsl:attribute>
                                <xsl:attribute name="scriptcode">
                                    <xsl:value-of
                                        select="$langfile//none:language[@code=normalize-space(current()/@langcode)]/none:scriptcode"
                                    />
                                </xsl:attribute>
                                <xsl:apply-templates select="node()" mode="#current"/>
                            </language>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:apply-templates select="node()" mode="#current"/>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <xsl:choose>
            <xsl:when test="normalize-space($langmaterialtemp)">
                <xsl:copy-of select="$langmaterialtemp"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:apply-templates select="node()" mode="#current"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <!-- copy: repository -->
    <xsl:template match="repository" name="repository" mode="copy">
        <xsl:choose>
            <xsl:when test="text()[string-length(normalize-space(.)) ge 1] or count(child::*) != 0">
                <repository>
                    <xsl:apply-templates mode="#current"/>
                </repository>
            </xsl:when>
            <xsl:otherwise>
                <repository>
                    <xsl:value-of select="$repository"/>
                </repository>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <!-- fonds intermediate lowest: repository -->
    <xsl:template match="repository" mode="fonds intermediate lowest">
        <!--<xsl:choose>-->
        <!--<xsl:when test="extref/corpname and not(address/addressline)">  &lt;!&ndash;Added address/addressline but to test with FR data&ndash;&gt;-->
        <!--<repository>-->
        <!--<address>-->
        <!--<addressline>-->
        <!--<xsl:apply-templates select="node()" mode="#current" />-->
        <!--<xsl:value-of select="extref/corpname"/>-->
        <!--<xsl:if test="extref/@href">-->
        <!--<xsl:text> (</xsl:text><xsl:value-of select="extref/@href"/><xsl:text>)</xsl:text>-->
        <!--</xsl:if>-->
        <!--</addressline>-->
        <!--</address>-->
        <!--</repository>-->
        <!--</xsl:when>-->
        <!--<xsl:otherwise>-->
        <repository>
            <xsl:apply-templates select="node()" mode="#current"/>
        </repository>
        <!--</xsl:otherwise>-->
        <!--</xsl:choose>-->
    </xsl:template>

    <!-- fonds intermediate lowest: repository/address -->
    <xsl:template match="repository/address" mode="copy fonds intermediate lowest">
        <address>
            <xsl:apply-templates select="node()" mode="#current"/>
        </address>
    </xsl:template>

    <!-- fonds intermediate lowest: repository/address/addressline -->
    <xsl:template match="repository/address/addressline" mode="copy fonds intermediate lowest">
        <addressline>
            <xsl:apply-templates select="node()" mode="#current"/>
            <xsl:if test="../../extref/corpname">
                <xsl:value-of select="../../extref/corpname"/>
                <xsl:if test="../../extref/@href">
                    <xsl:text> (</xsl:text>
                    <xsl:value-of select="ape:checkLink(extref/@href)"/>
                    <xsl:text>)</xsl:text>
                </xsl:if>
            </xsl:if>
        </addressline>
    </xsl:template>

    <!-- Used for the ALs -->
    <xsl:template match="repository/extref" mode="copy fonds intermediate lowest">
        <xsl:if test="not(../../address/addressline)">
            <extref>
                <xsl:if test="@href">
                    <xsl:attribute name="xlink:href" select="ape:checkLink(@href)"/>
                </xsl:if>
                <xsl:if test="@xlink:href">
                    <xsl:attribute name="xlink:href" select="ape:checkLink(@xlink:href)"/>
                </xsl:if>
                <xsl:apply-templates select="node()" mode="#current"/>
            </extref>
        </xsl:if>
    </xsl:template>

    <xsl:template match="repository/extref/corpname" mode="fonds intermediate lowest">
        <xsl:if test="not(../../address/addressline)">
            <xsl:apply-templates select="node()" mode="#current"/>
        </xsl:if>
    </xsl:template>

    <!-- fonds intermediate lowest: repository/corpname -->
    <xsl:template match="repository/corpname" mode="copy fonds intermediate lowest">
        <corpname>
            <xsl:apply-templates select="node()" mode="#current"/>
        </corpname>
    </xsl:template>

    <!-- fonds intermediate lowest: repository/name -->
    <xsl:template match="repository/name" mode="copy fonds intermediate lowest">
        <name>
            <xsl:apply-templates select="node()" mode="#current"/>
        </name>
    </xsl:template>

    <!-- copy fonds intermediate lowest: physloc -->
    <xsl:template match="physloc" mode="copy fonds intermediate lowest">
        <physloc>
            <xsl:if test="@label!=''">
                <xsl:attribute name="label" select="@label"/>
            </xsl:if>
            <xsl:value-of select="normalize-space(.)"/>
        </physloc>
    </xsl:template>

    <!-- copy fonds intermediate lowest: materialspec -->
    <xsl:template match="materialspec" mode="copy fonds intermediate lowest">
        <materialspec>
            <xsl:value-of select="normalize-space(.)"/>
        </materialspec>
    </xsl:template>

    <!-- copy archdesc/did/dao -->
    <xsl:template match="archdesc/did/dao" mode="copy">
        <dao>
            <xsl:if test="@*:href!=''">
                <xsl:attribute name="xlink:href" select="ape:checkLink(@*:href)"/>
            </xsl:if>
            <xsl:if test="@*:title!=''">
                <xsl:attribute name="xlink:title" select="@*:title"/>
            </xsl:if>
        </dao>
    </xsl:template>

    <!-- copy fonds intermediate lowest: physdesc -->
    <xsl:template match="physdesc" mode="copy fonds intermediate lowest">
        <physdesc encodinganalog="3.1.5">
            <xsl:apply-templates select="node()" mode="copy"/>
        </physdesc>
    </xsl:template>

    <!-- copy intermediate lowest: physdesc/physfacet -->
    <xsl:template match="physdesc/physfacet" mode="copy fonds intermediate lowest">
        <physfacet>
            <xsl:if test="@type='condition' or @type='damage'">
                <xsl:attribute name="type" select="@type"/>
            </xsl:if>
            <xsl:apply-templates select="node()" mode="#current"/>
        </physfacet>
    </xsl:template>

    <!-- copy fonds intermediate lowest: physdesc/extent -->
    <xsl:template match="physdesc/extent" mode="copy fonds intermediate lowest">
        <xsl:choose>
            <xsl:when test="text() = '0' or not(normalize-space(.))"/>
            <xsl:otherwise>
                <extent>
                    <xsl:if test="@unit">
                        <xsl:attribute name="unit" select="@unit"/>
                    </xsl:if>
                    <xsl:apply-templates select="text()" mode="#current"/>
                </extent>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <!-- copy fonds intermediate lowest: physdesc/dimensions -->
    <xsl:template match="physdesc/dimensions" mode="copy fonds intermediate lowest">
        <dimensions>
            <xsl:if test="@unit">
                <xsl:attribute name="unit" select="@unit"/>
            </xsl:if>
            <xsl:if test="@type">
                <xsl:attribute name="type" select="@type"/>
            </xsl:if>
            <xsl:apply-templates select="node() except dimensions" mode="#current"/>
            <xsl:for-each select="dimensions">
                <xsl:apply-templates select="node()" mode="#current"/>
            </xsl:for-each>
        </dimensions>
    </xsl:template>

    <!-- copy fonds intermediate lowest: physdesc/genreform -->
    <xsl:template match="physdesc/genreform" mode="copy fonds intermediate lowest">
        <genreform>
            <xsl:apply-templates select="node()" mode="#current"/>
        </genreform>
    </xsl:template>


    <!--Beginning of big chunk-->
    <!--Elements in archdesc and c@fonds-->
    <!-- acqinfo -->
    <xsl:template match="acqinfo" mode="copy fonds intermediate lowest" priority="2">
        <acqinfo encodinganalog="3.2.4">
            <xsl:apply-templates select="node() except acqinfo" mode="#current"/>
            <xsl:apply-templates select="acqinfo" mode="nested"/>
        </acqinfo>
    </xsl:template>

    <xsl:template match="acqinfo" mode="nested" priority="2">
        <xsl:apply-templates select="node()" mode="#current"/>
    </xsl:template>

    <!-- relatedmaterial -->
    <xsl:template match="relatedmaterial" mode="copy fonds intermediate lowest">
        <xsl:choose>
            <xsl:when test="not(child::*)"/>
            <xsl:otherwise>
                <relatedmaterial encodinganalog="3.5.3">
                    <xsl:apply-templates select="node() except relatedmaterial" mode="#current"/>
                    <xsl:apply-templates select="relatedmaterial/*" mode="nested"/>
                </relatedmaterial>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <!-- relatedmaterial/archref -->
    <xsl:template match="relatedmaterial/archref | relatedmaterial/ref"
                  mode="copy fonds intermediate lowest nested">
        <p>
            <xsl:for-each select="./title">
                <xsl:apply-templates mode="#current"/>
            </xsl:for-each>
            <extref>
                <xsl:if test="@*:href!='' and not(contains(@*:href, '['))">
                    <xsl:attribute name="xlink:href" select="ape:checkLink(@*:href)"/>
                </xsl:if>
                <xsl:if test="@*:target!=''">
                    <xsl:attribute name="xlink:href" select="ape:checkLink(@*:target)"/>
                </xsl:if>
                <xsl:if test="@*:title!=''">
                    <xsl:attribute name="xlink:title" select="@*:title"/>
                </xsl:if>
                <xsl:apply-templates select="node()" mode="#current"/>
            </extref>
        </p>
    </xsl:template>

    <xsl:template match="relatedmaterial/archref/title" mode="copy fonds intermediate lowest nested"/>
    <xsl:template match="relatedmaterial/archref/unitid"
                  mode="copy fonds intermediate lowest nested">
        <xsl:text>, </xsl:text>
        <xsl:apply-templates mode="#current"/>
    </xsl:template>
    <xsl:template match="relatedmaterial/archref/unittitle"
                  mode="copy fonds intermediate lowest nested">
        <xsl:apply-templates mode="#current"/>
    </xsl:template>
    <xsl:template match="relatedmaterial/archref/unittitle/ref"
                  mode="copy fonds intermediate lowest nested">
        <xsl:apply-templates mode="#current"/>
    </xsl:template>
    <xsl:template match="relatedmaterial/archref/repository"
                  mode="copy fonds intermediate lowest nested">
        <xsl:apply-templates mode="#current"/>
    </xsl:template>
    <xsl:template match="relatedmaterial/archref/repository/extref"
                  mode="copy fonds intermediate lowest nested">
        <xsl:apply-templates mode="#current"/>
    </xsl:template>
    <xsl:template match="relatedmaterial/p/repository" mode="copy fonds intermediate lowest nested">
        <xsl:apply-templates mode="#current"/>
    </xsl:template>

    <!-- archdesc/altformavail -->
    <xsl:template match="altformavail" mode="copy fonds intermediate lowest">
        <altformavail encodinganalog="3.5.2">
            <xsl:if test="text()">
                <p>
                    <xsl:value-of select="text()"/>
                </p>
            </xsl:if>
            <xsl:apply-templates select="node() except altformavail, text()" mode="#current"/>
            <xsl:apply-templates select="altformavail/*" mode="nested"/>
        </altformavail>
    </xsl:template>

    <!-- archdesc/separatedmaterial -->
    <xsl:template match="separatedmaterial" mode="copy fonds intermediate lowest" priority="2">
        <separatedmaterial encodinganalog="3.5.3">
            <xsl:apply-templates select="node() except separatedmaterial" mode="#current"/>
            <xsl:apply-templates select="separatedmaterial/*" mode="nested"/>
        </separatedmaterial>
    </xsl:template>

    <!-- otherfindaid -->
    <!--otherfindaid/bibref is below with bibliography-->
    <xsl:template match="otherfindaid" mode="copy fonds intermediate lowest">
        <otherfindaid encodinganalog="3.4.5">
            <xsl:apply-templates select="node() except otherfindaid" mode="#current"/>
            <xsl:apply-templates select="otherfindaid/*" mode="nested"/>
            <xsl:for-each select="list/item">
                <p>
                    <extref>
                        <xsl:attribute name="xlink:href">
                            <xsl:choose>
                                <xsl:when test="contains(archref/@href, '.xml')">
                                    <xsl:value-of
                                        select="substring-before(tokenize(archref/@href, '/')[last()], '.xml')"
                                    />
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:value-of select="tokenize(archref/@href, '/')[last()]"/>
                                </xsl:otherwise>
                            </xsl:choose>
                        </xsl:attribute>
                    </extref>
                </p>
            </xsl:for-each>
        </otherfindaid>
    </xsl:template>

    <!-- prefercite -->
    <xsl:template match="prefercite" mode="copy fonds intermediate lowest" priority="2">
        <prefercite>
            <xsl:apply-templates select="node() except prefercite" mode="#current"/>
            <xsl:apply-templates select="prefercite/*" mode="nested"/>
        </prefercite>
    </xsl:template>

    <!-- archdesc/arrangement -->
    <xsl:template match="arrangement" mode="copy fonds intermediate lowest" priority="2">
        <arrangement encodinganalog="3.3.4">
            <xsl:apply-templates select="node() except arrangement" mode="#current"/>
            <xsl:apply-templates select="arrangement" mode="nested"/>
        </arrangement>
    </xsl:template>

    <xsl:template match="arrangement" mode="nested" priority="2">
        <xsl:apply-templates select="node()" mode="#current"/>
    </xsl:template>

    <!-- archdesc/originalsloc (2x ?)-->
    <xsl:template match="originalsloc" mode="copy fonds intermediate lowest" priority="2">
        <originalsloc encodinganalog="3.5.1">
            <xsl:apply-templates select="node() except originalsloc" mode="#current"/>
            <xsl:apply-templates select="originalsloc/*" mode="nested"/>
        </originalsloc>
    </xsl:template>

    <!-- fileplan -->
    <xsl:template match="fileplan" mode="copy fonds intermediate lowest">
        <fileplan>
            <xsl:apply-templates select="node() except fileplan" mode="#current"/>
            <xsl:apply-templates select="fileplan/*" mode="nested"/>
        </fileplan>
    </xsl:template>

    <!-- fileplan/p/archref -->
    <xsl:template match="fileplan/p/archref" mode="copy fonds nested intermediate lowest">
        <archref>
            <xsl:if test="@*:href!=''">
                <xsl:attribute name="xlink:href" select="ape:checkLink(@*:href)"/>
            </xsl:if>
            <xsl:if test="@*:title!=''">
                <xsl:attribute name="xlink:title" select="@*:title"/>
            </xsl:if>
            <xsl:apply-templates select="node()" mode="#current"/>
        </archref>
    </xsl:template>

    <!-- archref/dao This is being erased as of 02/10/10 request from Germany -->
    <!--<xsl:template match="archref/dao" mode="copy fonds nested">-->
    <!--<dao>-->
    <!--<xsl:if test="@xlink:href!=''">-->
    <!--<xsl:attribute name="xlink:href" select="@xlink:href"/>-->
    <!--</xsl:if>-->
    <!--<xsl:if test="@xlink:title!=''">-->
    <!--<xsl:attribute name="xlink:title" select="@xlink:title"/>-->
    <!--</xsl:if>-->
    <!--</dao>-->
    <!--</xsl:template>-->

    <!--Elements in archdesc, c@fonds, c@intermediate and c@lowest-->
    <!-- processinfo -->
    <xsl:template match="processinfo[not(ancestor::processinfo)]"
                  mode="copy fonds intermediate lowest">
        <xsl:if test="count(child::*) != 0 or normalize-space(text()) != ''">
            <processinfo encodinganalog="3.7.1">
                <xsl:apply-templates select="node() except processinfo" mode="#current"/>
                <xsl:apply-templates select="processinfo/*" mode="nested"/>
            </processinfo>
        </xsl:if>
    </xsl:template>

    <xsl:template match="processinfo[ancestor::processinfo]" mode="copy fonds intermediate lowest"/>

    <xsl:template match="processinfo[ancestor::processinfo]" mode="nested">
        <xsl:apply-templates select="node()" mode="#current"/>
    </xsl:template>

    <!-- bioghist -->
    <xsl:template match="bioghist" mode="copy fonds intermediate lowest">
        <bioghist encodinganalog="3.2.2">
            <xsl:apply-templates select="node() except bioghist" mode="#current"/>
            <xsl:apply-templates select="bioghist" mode="nested"/>
        </bioghist>
    </xsl:template>

    <xsl:template match="bioghist" mode="nested">
        <xsl:apply-templates select="node()" mode="#current"/>
    </xsl:template>

    <!--bioghist/dao ONLY on archdesc level!-->
    <xsl:template match="bioghist[not(ancestor::dsc)]/dao" mode="copy nested">
        <dao>
            <xsl:if test="@*:href!=''">
                <xsl:attribute name="xlink:href" select="ape:checkLink(@*:href)"/>
            </xsl:if>
            <xsl:if test="@*:title!=''">
                <xsl:attribute name="xlink:title" select="@*:title"/>
            </xsl:if>
        </dao>
    </xsl:template>

    <!-- appraisal -->
    <xsl:template match="appraisal" mode="copy fonds intermediate lowest">
        <appraisal encodinganalog="3.3.2">
            <xsl:apply-templates select="node() except appraisal" mode="#current"/>
            <xsl:apply-templates select="appraisal/*" mode="nested"/>
        </appraisal>
    </xsl:template>

    <!-- accruals -->
    <xsl:template match="accruals" mode="copy fonds intermediate lowest">
        <accruals encodinganalog="3.3.3">
            <xsl:apply-templates select="node() except accruals" mode="#current"/>
            <xsl:apply-templates select="accruals" mode="nested"/>
        </accruals>
    </xsl:template>

    <xsl:template match="accruals" mode="nested">
        <xsl:apply-templates select="node()" mode="#current"/>
    </xsl:template>

    <!-- odd -->
    <xsl:template match="odd" mode="copy fonds intermediate lowest">
        <xsl:if test="count(child::*) != 0 or normalize-space(text()) != ''">
            <odd encodinganalog="3.6.1">
                <xsl:choose>
                    <xsl:when test="count(child::*) = 0">
                        <p>
                            <xsl:apply-templates select="node()" mode="#current"/>
                        </p>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:apply-templates select="node() except odd" mode="#current"/>
                        <xsl:apply-templates select="odd/*" mode="nested"/>
                    </xsl:otherwise>
                </xsl:choose>
            </odd>
        </xsl:if>
    </xsl:template>

    <!-- accessrestrict -->
    <xsl:template match="accessrestrict[not(ancestor::accessrestrict)]"
                  mode="copy fonds intermediate lowest">
        <accessrestrict encodinganalog="3.4.1">
            <xsl:apply-templates select="node() except accessrestrict" mode="#current"/>
            <xsl:apply-templates select="accessrestrict/*" mode="nested"/>
        </accessrestrict>
    </xsl:template>

    <!-- userestrict -->
    <xsl:template match="userestrict" mode="copy fonds intermediate lowest">
        <userestrict encodinganalog="3.4.5">
            <xsl:apply-templates select="node() except userestrict" mode="#current"/>
            <xsl:apply-templates select="userestrict/*" mode="nested"/>
        </userestrict>
    </xsl:template>


    <!--HEAD-->
    <!-- #all -->
    <xsl:template
        match="processinfo/head | relatedmaterial/head | bioghist/head | appraisal/head | accruals/head | odd/head | otherfindaid/head | accessrestrict[not(ancestor::accessrestrict)]/head | userestrict/head | altformavail/head | custodhist/head"
        mode="copy fonds intermediate lowest">
        <xsl:choose>
            <xsl:when test="not(preceding-sibling::*/head)">
                <head>
                    <xsl:value-of select="text()"/>
                </head>
            </xsl:when>
            <xsl:otherwise>
                <p>
                    <emph render="bold">
                        <xsl:value-of select="text()"/>
                    </emph>
                </p>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    <!-- archdesc and c@fonds -->
    <xsl:template
        match="acqinfo/head | separatedmaterial/head | prefercite/head | arrangement/head | originalsloc/head | fileplan/head"
        mode="copy fonds intermediate lowest">
        <head>
            <xsl:value-of select="text()"/>
        </head>
    </xsl:template>
    <!-- #all NESTED mode-->
    <xsl:template
        match="processinfo/head | bioghist/head | appraisal/head | accruals/head | odd/head | accessrestrict/head | userestrict/head | custodhist/head"
        mode="nested">
        <p>
            <emph render="bold">
                <xsl:value-of select="text()"/>
            </emph>
        </p>
    </xsl:template>
    <!-- archdesc and c@fonds NESTED mode-->
    <xsl:template
        match="acqinfo/head | relatedmaterial/head | altformavail/head | separatedmaterial/head | otherfindaid/head | prefercite/head | arrangement/head | originalsloc/head | fileplan/head"
        mode="nested">
        <p>
            <emph render="bold">
                <xsl:value-of select="text()"/>
            </emph>
        </p>
    </xsl:template>

    <!--P-->
    <!-- #all -->
    <xsl:template
        match="processinfo/p | relatedmaterial/p | bioghist/p | appraisal/p | accruals/p | odd/p | accessrestrict/p | accessrestrict/legalstatus | userestrict/p | altformavail/p | otherfindaid/p | custodhist/p"
        mode="copy fonds intermediate lowest nested">
        <xsl:if
            test="(count(child::node()[not(name()='list' or name()='chronlist' or name()='table')]) &gt; 0) or (not(following-sibling::p) and not(preceding-sibling::list) and not(following-sibling::list) and not(preceding-sibling::chronlist) and not(following-sibling::chronlist) and not(preceding-sibling::table) and not(following-sibling::table))">
            <p>
                <xsl:apply-templates
                    select="node()[not(name()='list' or name()='chronlist' or name()='table')]"
                    mode="#current"/>
            </p>
        </xsl:if>
        <xsl:apply-templates select="list | chronlist | table" mode="#current"/>
    </xsl:template>

    <!-- copy: legalstatus/date -->
    <xsl:template match="accessrestrict[not(ancestor::accessrestrict)]/legalstatus/date" mode="copy">
        <xsl:apply-templates select="node()" mode="#current"/>
    </xsl:template>

    <!--archdesc and c@fonds-->
    <xsl:template
        match="acqinfo/p | separatedmaterial/p | prefercite/p | arrangement/p | originalsloc/p | fileplan/p"
        mode="copy fonds nested intermediate lowest">
        <xsl:if
            test="(count(child::node()[not(name()='list' or name()='chronlist' or name()='table')]) &gt; 0) or (not(following-sibling::p) and not(preceding-sibling::list) and not(following-sibling::list) and not(preceding-sibling::chronlist) and not(following-sibling::chronlist) and not(preceding-sibling::table) and not(following-sibling::table))">
            <p>
                <!--not(preceding-sibling::p) and -->
                <xsl:apply-templates
                    select="node()[not(name()='list' or name()='chronlist' or name()='table')]"
                    mode="#current"/>
            </p>
        </xsl:if>
        <xsl:apply-templates select="list | chronlist | table" mode="#current"/>
    </xsl:template>

    <xsl:template match="otherfindaid/p/ref" mode="fonds intermediate lowest">
        <xsl:apply-templates mode="#current"/>
    </xsl:template>
    <xsl:template match="otherfindaid/p/ref/archref/note | otherfindaid/p/ref/note"
                  mode="fonds intermediate lowest">
        <xsl:apply-templates mode="#current"/>
    </xsl:template>
    <xsl:template match="otherfindaid/p/ref/archref/note/p | otherfindaid/p/ref/note/p"
                  mode="fonds intermediate lowest" priority="1">
        <xsl:apply-templates mode="#current"/>
    </xsl:template>
    <xsl:template match="otherfindaid/p/ref/archref/note/p/emph | otherfindaid/p/ref/note/p/emph"
                  mode="fonds intermediate lowest" priority="1">
        <xsl:apply-templates mode="#current"/>
    </xsl:template>

    <xsl:template match="otherfindaid/p/archref | otherfindaid/p/ref/archref"
                  mode="fonds intermediate lowest">
        <extref>
            <xsl:if test="@*:href">
                <xsl:attribute name="xlink:href" select="ape:checkLink(@*:href)"/>
            </xsl:if>
            <xsl:if test="@href">
                <xsl:attribute name="xlink:href" select="ape:checkLink(@href)"/>
            </xsl:if>
            <xsl:if test="@*:title">
                <xsl:attribute name="xlink:title" select="@*:title"/>
            </xsl:if>
            <xsl:if test="@title">
                <xsl:attribute name="xlink:title" select="@title"/>
            </xsl:if>
            <xsl:apply-templates select="node()" mode="#current"/>
        </extref>
    </xsl:template>

    <!--LIST-->
    <!-- #all -->
    <xsl:template
        match="processinfo/list | relatedmaterial/list | relatedmaterial/p/list | bioghist/list | bioghist/p/list | appraisal/list | accruals/list | odd/list | odd/p/list | accessrestrict[not(ancestor::accessrestrict)]/list | userestrict/list | altformavail/list | otherfindaid/list | custodhist/list | bibliography/list"
        mode="copy fonds intermediate lowest nested" name="p_list">
        <xsl:choose>
            <xsl:when test="@type='deflist' or (not(@type) and ./child::*[name()='defitem'])">
                <table>
                    <xsl:call-template name="deflist_table"/>
                </table>
            </xsl:when>
            <xsl:otherwise>
                <xsl:if test="not(parent::otherfindaid and item/archref[@href])">
                    <list>
                        <xsl:if test="@type='ordered' or @type='marked'">
                            <xsl:attribute name="type" select="@type"/>
                        </xsl:if>
                        <xsl:if test="@type='simple'">
                            <xsl:attribute name="type" select="'marked'"/>
                        </xsl:if>
                        <xsl:if test="@type='ordered'">
                            <xsl:attribute name="numeration" select="'arabic'"/>
                        </xsl:if>
                        <xsl:if test="not(./item)">
                            <item/>
                        </xsl:if>
                        <xsl:apply-templates select="node()" mode="#current"/>
                    </list>
                </xsl:if>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    <!-- archdesc and c@fonds -->
    <xsl:template
        match="acqinfo/list | acqinfo/p/list | separatedmaterial/list | prefercite/list | arrangement/list | arrangement/p/list | originalsloc/list | fileplan/list"
        mode="copy fonds nested intermediate lowest">
        <xsl:choose>
            <xsl:when test="@type='deflist' or (not(@type) and ./child::*[name()='defitem'])">
                <table>
                    <xsl:call-template name="deflist_table"/>
                </table>
            </xsl:when>
            <xsl:otherwise>
                <list>
                    <xsl:if test="@type='ordered' or @type='marked'">
                        <xsl:attribute name="type" select="@type"/>
                    </xsl:if>
                    <xsl:if test="@type='ordered'">
                        <xsl:attribute name="numeration" select="'arabic'"/>
                    </xsl:if>
                    <xsl:apply-templates select="node()" mode="#current"/>
                </list>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <!--HEAD-->
    <!-- #all -->
    <xsl:template
        match="processinfo/list/head | relatedmaterial/list/head | relatedmaterial/p/list/head | bioghist/list/head | bioghist/p/list/head | appraisal/list/head | accruals/list/head | odd/list/head | odd/p/list/head | accessrestrict[not(ancestor::accessrestrict)]/list/head | userestrict/list/head | altformavail/list/head | otherfindaid/list/head | custodhist/list/head | bibliography/list/head"
        mode="copy fonds intermediate lowest nested">
        <head>
            <xsl:apply-templates select="node()" mode="#current"/>
        </head>
    </xsl:template>
    <!-- archdesc and c@fonds -->
    <xsl:template
        match="acqinfo/list/head | acqinfo/p/list/head | separatedmaterial/list/head | prefercite/list/head | arrangement/list/head | arrangement/p/list/head | originalsloc/list/head | fileplan/list/head"
        mode="copy fonds nested intermediate lowest">
        <head>
            <xsl:apply-templates select="node()" mode="#current"/>
        </head>
    </xsl:template>

    <!--ITEM-->
    <!-- #all -->
    <xsl:template
        match="processinfo/list/item | relatedmaterial/list/item | relatedmaterial/p/list/item | bioghist/list/item | bioghist/p/list/item | odd/p/list/item | appraisal/list/item | accruals/list/item | odd/list/item | accessrestrict[not(ancestor::accessrestrict)]/list/item | userestrict/list/item | altformavail/list/item | otherfindaid/list/item | custodhist/list/item | bibliography/list/item"
        mode="copy fonds intermediate lowest nested">
        <item>
            <xsl:value-of select="node()"/>
            <xsl:apply-templates select="extref" mode="#current"/>
        </item>
    </xsl:template>
    <!-- archdesc and c@fonds -->
    <xsl:template
        match="acqinfo/list/item | acqinfo/p/list/item | separatedmaterial/list/item | prefercite/list/item | arrangement/list/item | arrangement/p/list/item | originalsloc/list/item | fileplan/list/item"
        mode="copy fonds nested intermediate lowest">
        <item>
            <xsl:value-of select="node()"/>
            <xsl:apply-templates select="extref" mode="#current"/>
        </item>
    </xsl:template>

    <!--ITEM//*-->
    <!-- #all -->
    <xsl:template
        match="processinfo/list/item//* | relatedmaterial/list/item//* | relatedmaterial/p/list/item//* | bioghist/list/item//* | appraisal/list/item//* | accruals/list/item//* | odd/list/item//* | odd/p/list/item//*[not(local-name='extref')] | accessrestrict[not(ancestor::accessrestrict)]/list/item//* | userestrict/list/item//* | altformavail/list/item//* | otherfindaid/list/item//* | custodhist/list/item//* | bibliography/list/item//*"
        mode="copy fonds intermediate lowest nested">
        <xsl:value-of select="text()"/>
    </xsl:template>

    <!-- archdesc and c@fonds -->
    <xsl:template
        match="acqinfo/list/item//* | acqinfo/p/list/item//* | separatedmaterial/list/item//* | prefercite/list/item//* | arrangement/list/item//* | arrangement/p/list/item//* | originalsloc/list/item//* | fileplan/list/item//*"
        mode="copy fonds nested intermediate lowest">
        <xsl:value-of select="text()"/>
    </xsl:template>

    <!-- ITEM/extref (gets higher priority due to avoiding ambiguous matching) -->
    <xsl:template match="extref[parent::item]" mode="copy fonds intermediate lowest nested"
                  priority="5">
        <extref>
            <xsl:if test="@href">
                <xsl:attribute name="xlink:href" select="ape:checkLink(@href)"/>
            </xsl:if>
            <xsl:if test="@*:href">
                <xsl:attribute name="xlink:href" select="ape:checkLink(@*:href)"/>
            </xsl:if>
            <xsl:if test="@title">
                <xsl:attribute name="xlink:title" select="@title"/>
            </xsl:if>
            <xsl:if test="@*:title">
                <xsl:attribute name="xlink:title" select="@*:title"/>
            </xsl:if>
        </extref>
    </xsl:template>

    <xsl:template
        match="processinfo/p/note | separatedmaterial/p/note | bioghist/p/note | arrangement/p/note | acqinfo/p/note | accruals/p/note | custodhist/p/note"
        mode="copy fonds intermediate lowest nested">
        <xsl:text> (</xsl:text>
        <xsl:apply-templates select="p/text()" mode="#current"/>
        <xsl:text>) </xsl:text>
    </xsl:template>

    <xsl:template match="bioghist/note" mode="copy fonds intermediate lowest nested">
        <p>
            <xsl:text> (</xsl:text>
            <xsl:apply-templates select="p/text()" mode="#current"/>
            <xsl:text>) </xsl:text>
        </p>
    </xsl:template>

    <xsl:template match="userestrict/note | accessrestrict/note"
                  mode="copy fonds intermediate lowest nested">
        <xsl:apply-templates mode="#current"/>
    </xsl:template>

    <xsl:template match="userestrict/address" mode="copy fonds intermediate lowest nested">
        <xsl:apply-templates mode="#current"/>
    </xsl:template>

    <xsl:template match="userestrict/address/addressline"
                  mode="copy fonds intermediate lowest nested">
        <p>
            <xsl:apply-templates mode="#current"/>
        </p>
    </xsl:template>

    <!--CHRONLIST-->
    <xsl:template match="chronlist" mode="copy fonds intermediate lowest nested">
        <list type="marked">
            <xsl:apply-templates mode="#current"/>
        </list>
    </xsl:template>
    <!--CHRONLIST/HEAD-->
    <xsl:template match="chronlist/head" mode="copy fonds intermediate lowest nested">
        <head>
            <xsl:apply-templates mode="#current"/>
        </head>
    </xsl:template>
    <!--CHRONLIST/CHRONITEM-->
    <xsl:template match="chronlist/chronitem" mode="copy fonds intermediate lowest nested">
        <item>
            <xsl:value-of select="./event"/>
            <xsl:text>, </xsl:text>
            <xsl:value-of select="./date"/>
        </item>
    </xsl:template>

    <!--table-->
    <xsl:template match="table" name="table_all" mode="copy fonds intermediate lowest nested">
        <table>
            <xsl:apply-templates select="node()" mode="#current"/>
        </table>
    </xsl:template>
    <!--table/head-->
    <xsl:template match="table/head" mode="copy fonds intermediate lowest nested">
        <head>
            <xsl:apply-templates select="node()" mode="#current"/>
        </head>
    </xsl:template>
    <!--table/tgroup-->
    <xsl:template match="table/tgroup" mode="copy fonds intermediate lowest nested">
        <tgroup>
            <xsl:choose>
                <xsl:when test="@cols">
                    <xsl:attribute name="cols" select="@cols"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:attribute name="cols"
                                   select="count(./child::tbody[position()=1]/child::row[position()=1]/entry)"
                    />
                </xsl:otherwise>
            </xsl:choose>
            <xsl:apply-templates select="node()" mode="#current"/>
        </tgroup>
    </xsl:template>
    <!--table/tgroup/thead-->
    <xsl:template match="table/tgroup/thead" mode="copy fonds intermediate lowest nested">
        <thead>
            <xsl:apply-templates select="node()" mode="#current"/>
        </thead>
    </xsl:template>
    <!--table/tgroup/thead/row-->
    <xsl:template match="table/tgroup/thead/row" mode="copy fonds intermediate lowest nested">
        <row>
            <xsl:apply-templates select="node()" mode="#current"/>
        </row>
    </xsl:template>
    <!--table/tgroup/thead/row/entry-->
    <xsl:template match="table/tgroup/thead/row/entry" mode="copy fonds intermediate lowest nested">
        <entry>
            <xsl:apply-templates select="node()" mode="#current"/>
        </entry>
    </xsl:template>
    <!--table/tgroup/tbody-->
    <xsl:template match="table/tgroup/tbody" mode="copy fonds intermediate lowest nested">
        <tbody>
            <xsl:apply-templates select="node()" mode="#current"/>
        </tbody>
    </xsl:template>
    <!--table/tgroup/tbody/row-->
    <xsl:template match="table/tgroup/tbody/row" mode="copy fonds intermediate lowest nested">
        <row>
            <xsl:apply-templates select="node()" mode="#current"/>
        </row>
    </xsl:template>
    <!--table/tgroup/tbody/row/entry-->
    <xsl:template match="table/tgroup/tbody/row/entry" mode="copy fonds intermediate lowest nested">
        <entry>
            <xsl:apply-templates select="node()" mode="#current"/>
        </entry>
    </xsl:template>
    <!--table/tgroup/tbody/row/entry-->
    <xsl:template match="table/tgroup/tbody/row/entry/corpname | table/tgroup/tbody/row/entry/famname | table/tgroup/tbody/row/entry/function | table/tgroup/tbody/row/entry/genreform | table/tgroup/tbody/row/entry/geogname | table/tgroup/tbody/row/entry/name | table/tgroup/tbody/row/entry/occupation | table/tgroup/tbody/row/entry/persname | table/tgroup/tbody/row/entry/subject | table/tgroup/tbody/row/entry/title"
                  mode="copy fonds intermediate lowest nested">
        <xsl:apply-templates select="node()" mode="#current"/>
    </xsl:template>

    <xsl:template match="table/tgroup/tbody/row/entry/unittitle"
                  mode="copy fonds intermediate lowest nested">
        <xsl:apply-templates select="node()" mode="#current"/>
    </xsl:template>

    <xsl:template match="table/tgroup/tbody/row/entry/emph"
                  mode="copy fonds intermediate lowest nested">
        <xsl:apply-templates select="node()" mode="#current"/>
    </xsl:template>
    <!--End of big chunk-->

    <xsl:template match="p/unittitle" mode="copy fonds intermediate lowest nested">
        <xsl:apply-templates select="node()" mode="#current"/>
    </xsl:template>
    <xsl:template match="p/unitdate" mode="copy fonds intermediate lowest nested">
        <xsl:apply-templates select="node()" mode="#current"/>
    </xsl:template>
    <xsl:template match="p/bibref" mode="copy fonds intermediate lowest nested">
        <xsl:apply-templates select="node()" mode="#current"/>
    </xsl:template>
    <xsl:template match="p/bibref/title" mode="copy fonds intermediate lowest nested">
        <xsl:apply-templates select="node()" mode="#current"/>
    </xsl:template>

    <!-- copy fonds intermediate: scopecontent[not(@*)] -->
    <xsl:template match="scopecontent[not(@encodinganalog='preface' or @encodinganalog='Vorwort')]"
                  mode="copy fonds intermediate lowest">
        <xsl:if test="count(child::*) != 0 or normalize-space(text()) != ''">
            <xsl:if test="not(count(child::*) eq 1 and child::arrangement)">
                <scopecontent encodinganalog="summary">
                    <xsl:apply-templates
                        select="node()[not(name()='arrangement' or name()='scopecontent')]"
                        mode="#current"/>
                    <xsl:apply-templates select="scopecontent" mode="nested"/>
                </scopecontent>
            </xsl:if>
            <xsl:apply-templates select="arrangement" mode="#current"/>
        </xsl:if>
    </xsl:template>

    <xsl:template match="scopecontent" mode="nested">
        <xsl:apply-templates select="node()" mode="nested"/>
    </xsl:template>

    <!-- copy fonds intermediate: scopecontent[@encodinganalog='preface'] -->
    <xsl:template match="scopecontent[@encodinganalog='preface' or @encodinganalog='Vorwort']"
                  mode="copy fonds intermediate lowest">
        <scopecontent encodinganalog="preface">
            <xsl:apply-templates select="node()[not(name()='arrangement' or name()='scopecontent')]"
                                 mode="#current"/>
            <xsl:apply-templates select="scopecontent" mode="nested"/>
        </scopecontent>
        <xsl:apply-templates select="arrangement" mode="#current"/>
    </xsl:template>

    <!-- copy fonds intermediate: scopecontent/head -->
    <xsl:template match="scopecontent/head" mode="copy fonds intermediate lowest">
        <head>
            <xsl:apply-templates select="node()" mode="#current"/>
        </head>
    </xsl:template>

    <!-- nested: scopecontent/head -->
    <xsl:template match="scopecontent/head" mode="nested">
        <p>
            <emph render="bold">
                <xsl:value-of select="normalize-space(.)"/>
            </emph>
        </p>
    </xsl:template>

    <!-- copy fonds intermediate: scopecontent/p -->
    <xsl:template match="scopecontent/p" mode="copy fonds intermediate lowest nested">
        <p>
            <xsl:apply-templates
                select="node()[not(name()='list' or name()='chronlist' or name()='table')]"
                mode="#current"/>
        </p>
        <xsl:for-each select="list">
            <xsl:call-template name="p_list_scopecontent"/>
        </xsl:for-each>
        <xsl:for-each select="table">
            <xsl:call-template name="table_all"/>
        </xsl:for-each>
        <xsl:apply-templates select="chronlist" mode="#current"/>
    </xsl:template>

    <!-- copy fonds intermediate: scopecontent/p/note -->
    <xsl:template match="scopecontent/p/note" mode="copy fonds intermediate lowest nested">
        <xsl:text> (</xsl:text>
        <xsl:apply-templates select="p/text()" mode="#current"/>
        <xsl:text>) </xsl:text>
    </xsl:template>

    <xsl:template match="scopecontent/note | odd/note" mode="copy fonds intermediate lowest nested">
        <p>
            <xsl:text> (</xsl:text>
            <xsl:apply-templates select="p/text()" mode="#current"/>
            <xsl:text>) </xsl:text>
        </p>
    </xsl:template>

    <!-- copy fonds intermediate: scopecontent/list/item/list scopecontent/p/list/item/list -->
    <xsl:template
        match="scopecontent/list/item/list | scopecontent/p/list/item/list | scopecontent/list/item/list/item/list | scopecontent/p/list/item/list/item/list"
        mode="copy fonds intermediate lowest nested">
        <xsl:call-template name="p_list_scopecontent"/>
    </xsl:template>

    <!-- copy fonds intermediate: scopecontent/list | scopecontent/p/list -->
    <xsl:template match="scopecontent/list" name="p_list_scopecontent"
                  mode="copy fonds intermediate lowest nested">
        <xsl:choose>
            <xsl:when test="@type='deflist' or (not(@type) and ./child::*[name()='defitem'])">
                <table>
                    <xsl:call-template name="deflist_table"/>
                </table>
            </xsl:when>
            <xsl:otherwise>
                <list>
                    <xsl:if test="@type='ordered' or @type='marked'">
                        <xsl:attribute name="type" select="@type"/>
                    </xsl:if>
                    <xsl:if test="@type='ordered'">
                        <xsl:attribute name="numeration" select="'arabic'"/>
                    </xsl:if>
                    <xsl:apply-templates select="node()" mode="#current"/>
                </list>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <!-- copy fonds intermediate: scopecontent/list/item scopecontent/p/list/item | scopecontent/list/item/list/item | scopecontent/p/list/item/list/item -->
    <xsl:template
        match="scopecontent/list/item | scopecontent/p/list/item | scopecontent/list/item/list/item | scopecontent/p/list/item/list/item | scopecontent/list/item/list/item/list/item | scopecontent/p/list/item/list/item/list/item"
        mode="copy fonds intermediate lowest nested">
        <item>
            <xsl:apply-templates select="node()" mode="#current"/>
        </item>
    </xsl:template>

    <!-- copy fonds intermediate: scopecontent/list/item//* | scopecontent/p/list/item//* -->
    <xsl:template
        match="scopecontent/list/item/*[not(name()='list')] | scopecontent/p/list/item/*[not(name()='list')]"
        mode="copy fonds intermediate lowest nested">
        <xsl:apply-templates select="text()" mode="#current"/>
    </xsl:template>

    <!-- copy fonds intermediate: scopecontent/dao -->
    <xsl:template match="scopecontent/dao" mode="copy fonds intermediate lowest nested">
        <dao>
            <xsl:if test="@*:href!=''">
                <xsl:attribute name="xlink:href" select="ape:checkLink(@*:href)"/>
            </xsl:if>
            <xsl:if test="@*:title!=''">
                <xsl:attribute name="xlink:title" select="@*:title"/>
            </xsl:if>
        </dao>
    </xsl:template>

    <xsl:template match="scopecontent/address" mode="copy fonds intermediate lowest nested">
        <xsl:apply-templates mode="#current"/>
    </xsl:template>

    <xsl:template match="scopecontent/address/addressline"
                  mode="copy fonds intermediate lowest nested">
        <p>
            <xsl:apply-templates mode="#current"/>
        </p>
    </xsl:template>

    <!-- copy fonds intermediate lowest: bibliography -->
    <xsl:template match="bibliography" mode="copy fonds intermediate lowest">
        <bibliography encodinganalog="3.5.4">
            <xsl:apply-templates select="node()" mode="#current"/>
        </bibliography>
    </xsl:template>

    <!-- copy fonds intermediate lowest: bibliography/head -->
    <xsl:template match="bibliography/head" mode="copy fonds intermediate lowest">
        <head>
            <xsl:apply-templates select="text()" mode="#current"/>
        </head>
    </xsl:template>

    <!-- copy fonds intermediate lowest: bibliography/p -->
    <xsl:template match="bibliography/p" mode="copy fonds intermediate lowest">
        <p>
            <xsl:apply-templates select="node() except bibref" mode="#current"/>
            <xsl:for-each select="bibref">
                <xsl:apply-templates select="node()" mode="#current"/>
            </xsl:for-each>
        </p>
    </xsl:template>

    <!-- copy fonds intermediate lowest: bibliography/bibref -->
    <xsl:template match="bibliography/bibref" mode="copy fonds intermediate lowest">
        <bibref>
            <xsl:if test="@*:href">
                <xsl:attribute name="xlink:href" select="ape:checkLink(@*:href)"/>
            </xsl:if>
            <xsl:if test="@href">
                <xsl:attribute name="xlink:href" select="ape:checkLink(@href)"/>
            </xsl:if>
            <xsl:if test="@*:title">
                <xsl:attribute name="xlink:title" select="@*:title"/>
            </xsl:if>
            <xsl:if test="@title">
                <xsl:attribute name="xlink:title" select="@title"/>
            </xsl:if>
            <xsl:apply-templates select="node()" mode="#current"/>
        </bibref>
    </xsl:template>

    <xsl:template match="otherfindaid/bibref | relatedmaterial/bibref"
                  mode="copy fonds intermediate lowest nested">
        <p>
            <xsl:if test="@href or @*:href">
                <extref>
                    <xsl:if test="@*:href">
                        <xsl:attribute name="xlink:href" select="ape:checkLink(@*:href)"/>
                    </xsl:if>
                    <xsl:if test="@href">
                        <xsl:attribute name="xlink:href" select="ape:checkLink(@href)"/>
                    </xsl:if>
                    <xsl:if test="@*:title">
                        <xsl:attribute name="xlink:title" select="@*:title"/>
                    </xsl:if>
                    <xsl:if test="@title">
                        <xsl:attribute name="xlink:title" select="@title"/>
                    </xsl:if>
                    <xsl:apply-templates select="node()" mode="#current"/>
                </extref>
            </xsl:if>
            <xsl:if test="not(@href) and not(@*:href)">
                <xsl:apply-templates select="node()" mode="#current"/>
            </xsl:if>
        </p>
    </xsl:template>

    <xsl:template match="separatedmaterial/bibref" mode="copy fonds nested">
        <p>
            <xsl:if test="@href or @*:href">
                <extref>
                    <xsl:if test="@*:href">
                        <xsl:attribute name="xlink:href" select="ape:checkLink(@*:href)"/>
                    </xsl:if>
                    <xsl:if test="@href">
                        <xsl:attribute name="xlink:href" select="ape:checkLink(@href)"/>
                    </xsl:if>
                    <xsl:if test="@*:title">
                        <xsl:attribute name="xlink:title" select="@*:title"/>
                    </xsl:if>
                    <xsl:if test="@title">
                        <xsl:attribute name="xlink:title" select="@title"/>
                    </xsl:if>
                    <xsl:apply-templates select="node()" mode="#current"/>
                </extref>
            </xsl:if>
            <xsl:if test="not(@href) and not(@*:href)">
                <xsl:apply-templates select="node()" mode="#current"/>
            </xsl:if>
        </p>
    </xsl:template>
    <xsl:template match="separatedmaterial/bibref/title" mode="copy fonds nested">
        <title>
            <xsl:apply-templates select="node()" mode="#current"/>
        </title>
    </xsl:template>

    <xsl:template name="deflist_table">
        <tgroup cols="2">
            <tbody>
                <xsl:for-each select="defitem">
                    <row>
                        <xsl:call-template name="deflist_table_row"/>
                    </row>
                </xsl:for-each>
            </tbody>
        </tgroup>
    </xsl:template>
    <xsl:template name="deflist_table_row">
        <entry>
            <xsl:value-of select="label"/>
        </entry>
        <entry>
            <xsl:value-of select="item"/>
        </entry>
    </xsl:template>

    <xsl:template match="otherfindaid/bibref/extref | relatedmaterial/bibref/extref"
                  mode="copy fonds intermediate lowest nested">
        <extref>
            <xsl:if test="@href">
                <xsl:attribute name="xlink:href" select="ape:checkLink(@href)"/>
            </xsl:if>
            <xsl:if test="@*:href">
                <xsl:attribute name="xlink:href" select="ape:checkLink(@*:href)"/>
            </xsl:if>
            <xsl:apply-templates select="node()" mode="#current"/>
        </extref>
    </xsl:template>

    <xsl:template match="otherfindaid/extref" mode="copy fonds intermediate lowest nested">
        <p>
            <extref>
                <xsl:if test="@href">
                    <xsl:attribute name="xlink:href" select="ape:checkLink(@href)"/>
                </xsl:if>
                <xsl:if test="@*:href">
                    <xsl:attribute name="xlink:href" select="ape:checkLink(@*:href)"/>
                </xsl:if>
                <xsl:apply-templates select="node()" mode="#current"/>
            </extref>
        </p>
    </xsl:template>

    <xsl:template match="otherfindaid/extref/title" mode="copy fonds intermediate lowest nested">
        <xsl:apply-templates mode="#current"/>
    </xsl:template>

    <!-- copy fonds intermediate lowest: bibliography/bibref/name -->
    <xsl:template match="bibliography/bibref/name" mode="copy fonds intermediate lowest">
        <name>
            <xsl:apply-templates select="node()" mode="#current"/>
        </name>
    </xsl:template>

    <!-- copy fonds intermediate lowest: bibliography/bibref/title -->
    <xsl:template match="bibliography/bibref/title" mode="copy fonds intermediate lowest">
        <title>
            <xsl:apply-templates select="node()" mode="#current"/>
        </title>
    </xsl:template>

    <!-- copy fonds intermediate lowest: bibliography/bibref/imprint -->
    <xsl:template match="bibliography/bibref/imprint" mode="copy fonds intermediate lowest">
        <imprint>
            <xsl:apply-templates select="node()" mode="#current"/>
        </imprint>
    </xsl:template>

    <!-- copy fonds intermediate lowest: bibliography/bibref/imprint/publisher | bibliography/bibref/imprint/geogname | bibliography/bibref/imprint/date -->
    <xsl:template
        match="bibliography/bibref/imprint/publisher | bibliography/bibref/imprint/geogname | bibliography/bibref/imprint/date"
        mode="copy fonds intermediate lowest">
        <xsl:element name="{local-name()}" namespace="urn:isbn:1-931666-22-9">
            <xsl:apply-templates select="node()" mode="#current"/>
        </xsl:element>
    </xsl:template>

    <!-- phystech -->
    <xsl:template match="phystech" mode="#all">
        <phystech encodinganalog="3.4.4">
            <xsl:apply-templates mode="#current"/>
        </phystech>
    </xsl:template>

    <!-- phystech/head -->
    <xsl:template match="phystech/head" mode="#all">
        <head>
            <xsl:apply-templates mode="#current"/>
        </head>
    </xsl:template>

    <!-- phystech/p -->
    <xsl:template match="phystech/p" mode="#all">
        <p>
            <xsl:apply-templates mode="#current"/>
        </p>
    </xsl:template>

    <!-- copy fonds intermediate lowest: controlaccess -->
    <xsl:template match="controlaccess" name="controlaccess" mode="copy fonds intermediate lowest">
        <xsl:choose>
            <xsl:when test="not(child::*)"/>
            <xsl:when test="not(ancestor::controlaccess)">
                <xsl:choose>
                <xsl:when test="child::genreform[@type='typir'] and count(child::*) &gt; 1">
                    <xsl:if test="not(child::head and count(child::*) = 2)">
                        <controlaccess>
                        <!--../index/indexentry//geogname | ../index/indexentry//subject | ../index/indexentry//famname | ../index/indexentry//persname | ../index/indexentry//corpname | ../index/indexentry//occupation | ../index/indexentry//genreform | ../index/indexentry//function | ../index/indexentry//title | ../index/indexentry//name-->
                        <xsl:for-each
                            select="geogname | subject | famname | persname | corpname | occupation | genreform | function | title | p | head | name | indexentry//geogname | indexentry//subject | indexentry//famname | indexentry//persname | indexentry//corpname | indexentry//occupation | indexentry//genreform | indexentry//function | indexentry//title | indexentry//name | ../did//geogname[parent::item|parent::entry|parent::p|parent::unittitle] | ../did//subject[parent::item|parent::entry|parent::p|parent::unittitle] | ../did//famname[parent::item|parent::entry|parent::p|parent::unittitle] | ../did//persname[parent::item|parent::entry|parent::p|parent::unittitle] | ../did//corpname[parent::item|parent::entry|parent::p|parent::unittitle] | ../did//occupation[parent::item|parent::entry|parent::p|parent::unittitle] | ../did//genreform[parent::item|parent::entry|parent::p|parent::unittitle] | ../did//function[parent::item|parent::entry|parent::p|parent::unittitle] | ../did//title[parent::item|parent::entry|parent::p|parent::unittitle] | ../did//name[parent::item|parent::entry|parent::p|parent::unittitle]">
                            <xsl:if test="not(local-name()='genreform' and @type='typir')">
                                <xsl:element name="{local-name()}"
                                             namespace="urn:isbn:1-931666-22-9">
                                    <xsl:apply-templates select="node()" mode="#current"/>
                                </xsl:element>
                            </xsl:if>
                        </xsl:for-each>
                        <xsl:for-each select="controlaccess">
                            <xsl:call-template name="controlaccess"/>
                        </xsl:for-each>
                    </controlaccess>
                    </xsl:if>
                </xsl:when>
                    <xsl:otherwise>
                        <controlaccess>                                                                                                                           <!--../index/indexentry//geogname | ../index/indexentry//subject | ../index/indexentry//famname | ../index/indexentry//persname | ../index/indexentry//corpname | ../index/indexentry//occupation | ../index/indexentry//genreform | ../index/indexentry//function | ../index/indexentry//title | ../index/indexentry//name-->
                            <xsl:for-each select="geogname | subject | famname | persname | corpname | occupation | genreform | function | title | p | head | name | indexentry//geogname | indexentry//subject | indexentry//famname | indexentry//persname | indexentry//corpname | indexentry//occupation | indexentry//genreform | indexentry//function | indexentry//title | indexentry//name">
                                <xsl:if test="not(local-name()='genreform' and @type='typir')">
                                    <xsl:element name="{local-name()}" namespace="urn:isbn:1-931666-22-9">
                                        <xsl:apply-templates select="node()" mode="#current"/>
                                    </xsl:element>
                                </xsl:if>
                            </xsl:for-each>
                            <xsl:for-each select="controlaccess">
                                <xsl:call-template name="controlaccess"/>
                            </xsl:for-each>
                        </controlaccess>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:when>
            <xsl:otherwise>
                <xsl:for-each
                    select="geogname|subject|famname|persname|corpname|occupation|genreform|function|title|p|name | ../did//geogname[parent::item|parent::entry|parent::p|parent::unittitle] | ../did//subject[parent::item|parent::entry|parent::p|parent::unittitle] | ../did//famname[parent::item|parent::entry|parent::p|parent::unittitle] | ../did//persname[parent::item|parent::entry|parent::p|parent::unittitle] | ../did//corpname[parent::item|parent::entry|parent::p|parent::unittitle] | ../did//occupation[parent::item|parent::entry|parent::p|parent::unittitle] | ../did//genreform[parent::item|parent::entry|parent::p|parent::unittitle] | ../did//function[parent::item|parent::entry|parent::p|parent::unittitle] | ../did//title[parent::item|parent::entry|parent::p|parent::unittitle] | ../did//name[parent::item|parent::entry|parent::p|parent::unittitle]">
                    <xsl:if test="not(local-name()='genreform' and @type='typir')">
                        <xsl:element name="{local-name()}" namespace="urn:isbn:1-931666-22-9">
                            <xsl:apply-templates select="node()" mode="#current"/>
                        </xsl:element>
                    </xsl:if>
                </xsl:for-each>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template name="createControlaccess">
        <xsl:param name="context"/>
        <controlaccess>
            <xsl:for-each
                select="$context//geogname[parent::item|parent::entry|parent::p|parent::unittitle] | $context//subject[parent::item|parent::entry|parent::p|parent::unittitle] | $context//famname[parent::item|parent::entry|parent::p|parent::unittitle] | $context//persname[parent::item|parent::entry|parent::p|parent::unittitle] | $context//corpname[parent::item|parent::entry|parent::p|parent::unittitle] | $context//occupation[parent::item|parent::entry|parent::p|parent::unittitle] | $context//genreform[parent::item|parent::entry|parent::p|parent::unittitle] | $context//function[parent::item|parent::entry|parent::p|parent::unittitle] | $context//title[parent::item|parent::entry|parent::p|parent::unittitle] | $context//name[parent::item|parent::entry|parent::p|parent::unittitle]">
                <xsl:element name="{local-name()}" namespace="urn:isbn:1-931666-22-9">
                    <xsl:apply-templates select="node()" mode="#current"/>
                </xsl:element>
            </xsl:for-each>
        </controlaccess>
    </xsl:template>

    <!--<xsl:template name="createControlaccess">-->
    <!--<xsl:param name="context" />-->
    <!--<controlaccess>-->
    <!--<xsl:for-each select="$context/did/unittitle and $context/bibref">-->
    <!--<xsl:for-each select="geogname | subject | famname | persname | corpname | occupation | genreform | function">-->
    <!--<xsl:element name="{local-name()}" namespace="urn:isbn:1-931666-22-9">-->
    <!--<xsl:apply-templates select="node()" mode="#current"/>-->
    <!--</xsl:element>-->
    <!--</xsl:for-each>-->
    <!--</xsl:for-each>-->
    <!--</controlaccess>-->
    <!--</xsl:template>-->

    <!-- copy: dsc -->
    <xsl:template match="dsc" mode="copy">
        <dsc type="othertype">
            <xsl:apply-templates select="node()" mode="copy"/>
        </dsc>
    </xsl:template>

    <!-- fonds intermediate lowest: dsc -->
    <xsl:template match="dsc" mode="fonds intermediate lowest">
        <xsl:apply-templates select="node()" mode="#current"/>
    </xsl:template>

    <!-- copy: dsc/head -->
    <xsl:template match="dsc/head" mode="copy">
        <xsl:if test="preceding-sibling::* or following-sibling::*">
            <head>
                <xsl:value-of select="."/>
            </head>
        </xsl:if>
    </xsl:template>

    <!-- copy: dsc/p -->
    <xsl:template match="dsc/p" mode="copy">
        <p>
            <xsl:apply-templates select="node()" mode="copy"/>
        </p>
    </xsl:template>

    <xsl:template match="nothing" mode="lowest"/>
    <xsl:template match="nothing" mode="intermediate"/>
    <xsl:template match="nothing" mode="fonds"/>

    <!-- class: unittitle -->
    <xsl:template match="unittitle" mode="fonds intermediate lowest">
        <xsl:choose>
            <xsl:when test="(string-length(normalize-space(.)) lt 1) and ../unitdate">
                <unittitle>
                    <xsl:attribute name="encodinganalog" select="'3.1.2'"/>
                    <xsl:apply-templates select="../unitdate/node()" mode="#current"/>
                </unittitle>
            </xsl:when>
            <xsl:otherwise>
                <unittitle>
                    <xsl:attribute name="encodinganalog" select="'3.1.2'"/>
                    <xsl:if test="@type">
                        <xsl:attribute name="type" select="@type"/>
                    </xsl:if>
                    <xsl:apply-templates select="node()" mode="#current"/>
                </unittitle>
            </xsl:otherwise>
        </xsl:choose>
        <xsl:if test="./unitdate and not(../unitdate)">
            <xsl:for-each select="./unitdate">
                <unitdate>
                    <xsl:attribute name="encodinganalog" select="'3.1.3'"/>
                    <xsl:attribute name="era" select="'ce'"/>
                    <xsl:attribute name="calendar" select="'gregorian'"/>
                    <xsl:call-template name="normalizeDate"/>
                    <xsl:apply-templates select="node()" mode="#current"/>
                </unitdate>
            </xsl:for-each>
        </xsl:if>
    </xsl:template>
    <xsl:template match="unittitle/corpname | unittitle/famname | unittitle/function | unittitle/genreform | unittitle/geogname | unittitle/name | unittitle/occupation | unittitle/persname | unittitle/subject | unittitle/title"
                  mode="fonds intermediate lowest">
        <xsl:apply-templates select="node()" mode="#current"/>
    </xsl:template>

    <!-- class: unitid -->
    <xsl:template match="unitid" mode="fonds intermediate lowest">
        <xsl:choose>
            <xsl:when test="$countrycode='NL' and ../../@otherlevel='subfile'">
                <xsl:apply-templates select="../../../did/unitid" mode="#current"/>
            </xsl:when>
            <xsl:when
                test="(@type='call number' or @type='ABS' or @type='bestellnummer' or @type='Bestellnummer' or @type='series_code' or @type='reference' or @type='Sygnatura' or @type='REFERENCE_CODE' or @type='cote-de-consultation' or @type='cote-groupee' or @type='identifiant' or @type='cote' or @type='persistent' or (not(@type))) and (text()[string-length(normalize-space(.)) ge 1] or exists(extptr))">
                <!-- and not(preceding-sibling::unitid) and not(following-sibling::unitid)-->
                <xsl:choose>
                    <xsl:when test="@countrycode and @repositorycode">
                        <xsl:choose>
                            <xsl:when test="//eadid/@countrycode = 'SE'">
                                <xsl:variable name="eadid">
                                    <xsl:value-of select="//eadid/text()"/>
                                </xsl:variable>
                                <xsl:variable name="actual">
                                    <xsl:value-of select="."/>
                                </xsl:variable>
                                <xsl:variable name="above">
                                    <xsl:for-each select="ancestor::c/did/unitid">
                                        <xsl:if test="current() != $actual">
                                            <xsl:value-of select="."/>/ </xsl:if>
                                    </xsl:for-each>
                                </xsl:variable>
                                <unitid type="call number" encodinganalog="3.1.1">
                                    <xsl:if test="extptr">
                                        <xsl:apply-templates select="extptr" mode="#current"/>
                                    </xsl:if>
                                    <xsl:value-of
                                        select="concat(concat(concat(normalize-space($eadid), '/'), normalize-space($above)),.)"
                                    />
                                </unitid>
                            </xsl:when>
                            <xsl:otherwise>
                                <unitid type="call number" encodinganalog="3.1.1">
                                    <xsl:if test="extptr">
                                        <xsl:apply-templates select="extptr" mode="#current"/>
                                    </xsl:if>
                                    <xsl:choose>
                                        <xsl:when
                                            test="starts-with(., concat(concat(@countrycode,'/'),@repositorycode))">
                                            <xsl:value-of select="text()"/>
                                        </xsl:when>
                                        <xsl:otherwise>
                                            <xsl:value-of
                                                select="concat(concat(concat(concat(@countrycode,'/'),@repositorycode), '/'), .)"
                                            />
                                        </xsl:otherwise>
                                    </xsl:choose>
                                </unitid>
                            </xsl:otherwise>
                        </xsl:choose>
                    </xsl:when>
                    <xsl:otherwise>
                        <unitid>
                            <xsl:attribute name="type" select="'call number'"/>
                            <xsl:attribute name="encodinganalog" select="'3.1.1'"/>
                            <xsl:if test="extptr">
                                <xsl:apply-templates select="extptr" mode="#current"/>
                            </xsl:if>
                            <!-- For Netherlands -->
                            <xsl:if test="following-sibling::*/@type='handle'">
                                <extptr>
                                    <xsl:attribute name="xlink:href">
                                        <xsl:value-of
                                            select="following-sibling::*[@type='handle']/text()"/>
                                    </xsl:attribute>
                                </extptr>
                            </xsl:if>
                            <!--test-->
                            <xsl:choose>
                                <xsl:when test="//eadid[@countrycode='NL']">
                                    <xsl:choose>
                                        <xsl:when test="starts-with(., //eadid/text())">
                                            <xsl:value-of select="."/>
                                        </xsl:when>
                                        <xsl:otherwise>
                                            <xsl:value-of
                                                select="concat(//eadid/text(), concat(' - ', .))"/>
                                        </xsl:otherwise>
                                    </xsl:choose>
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:choose>
                                        <xsl:when
                                            test="starts-with(., //archdesc/did/unitid[1]/text()) or //archdesc/did/unitid[@label='Cotes extrêmes']">
                                            <xsl:value-of select="text()"/>
                                        </xsl:when>
                                        <xsl:otherwise>
                                            <xsl:value-of
                                                select="concat(//archdesc/did/unitid[1]/text(), concat(' - ', .))"
                                            />
                                        </xsl:otherwise>
                                    </xsl:choose>
                                </xsl:otherwise>
                            </xsl:choose>
                            <!--<xsl:apply-templates select="node()" mode="#current"/>-->
                        </unitid>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:when>
            <xsl:when
                test="@type='former call number' or @type='obsolete' or @type='former' or @type='altsignatur' or @type='Altsignatur' or @type='Sygnatura dawna' or @type='ancienne_cote' or @type='OLD_REFERENCE_CODE' or @type='nouvelle-foliotation' or @type='cote-forgée'">
                <xsl:if test="text()[string-length(normalize-space(.)) ge 1]">
                    <unitid>
                        <xsl:attribute name="type" select="'former call number'"/>
                        <xsl:attribute name="encodinganalog" select="'3.1.1'"/>
                        <xsl:if test="extptr">
                            <xsl:apply-templates select="extptr" mode="#current"/>
                        </xsl:if>
                        <xsl:apply-templates select="node()" mode="#current"/>
                    </unitid>
                </xsl:if>
            </xsl:when>
            <xsl:when
                test="@type='file reference' or @type='code-de-communication' or @type='Aktenzeichen' or @type='aktenzeichen' or @type='Znak teczki'">
                <xsl:if test="text()[string-length(normalize-space(.)) ge 1]">
                    <unitid>
                        <xsl:attribute name="type" select="'file reference'"/>
                        <xsl:attribute name="encodinganalog" select="'3.1.1'"/>
                        <xsl:if test="extptr">
                            <xsl:apply-templates select="extptr" mode="#current"/>
                        </xsl:if>
                        <xsl:apply-templates select="node()" mode="#current"/>
                    </unitid>
                </xsl:if>
            </xsl:when>
            <!--For Latvia - todo: check with them-->
            <!--<xsl:when test="not(@type) and string-length(normalize-space(.)) eq 0">-->
            <!--<unitid>-->
            <!--<xsl:attribute name="type" select="'call number'"/>-->
            <!--<xsl:attribute name="encodinganalog" select="'3.1.1'"/>-->
            <!--<xsl:value-of select="concat(//eadid/text(), ' - empty')" />-->
            <!--</unitid>-->
            <!--</xsl:when>-->
            <xsl:when test="@type='blank'"/>
            <xsl:otherwise>
                <xsl:call-template name="excludeElement"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    <!-- class: unitid/title -->
    <xsl:template match="unitid/title" mode="fonds intermediate lowest">
        <title>
            <xsl:apply-templates select="node()" mode="#current"/>
        </title>
    </xsl:template>
    <!-- fonds intermediate lowest: unitid/extptr -->
    <xsl:template match="unitid/extptr" mode="fonds intermediate lowest">
        <extptr>
            <xsl:if test="@href!=''">
                <xsl:attribute name="xlink:href" select="ape:checkLink(@href)"/>
            </xsl:if>
            <xsl:if test="@*:href!=''">
                <xsl:attribute name="xlink:href" select="ape:checkLink(@*:href)"/>
            </xsl:if>
            <xsl:if test="@title!=''">
                <xsl:attribute name="xlink:title" select="@title"/>
            </xsl:if>
            <xsl:if test="@*:title!=''">
                <xsl:attribute name="xlink:title" select="@*:title"/>
            </xsl:if>
            <xsl:apply-templates select="node()" mode="#current"/>
        </extptr>
    </xsl:template>

    <xsl:template match="unitid/extref" mode="copy fonds intermediate lowest">
        <extptr>
            <xsl:if test="@href!=''">
                <xsl:attribute name="xlink:href" select="ape:checkLink(@href)"/>
            </xsl:if>
            <xsl:if test="@*:href!=''">
                <xsl:attribute name="xlink:href" select="ape:checkLink(@*:href)"/>
            </xsl:if>
            <xsl:if test="@title!=''">
                <xsl:attribute name="xlink:title" select="@title"/>
            </xsl:if>
            <xsl:if test="@*:title!=''">
                <xsl:attribute name="xlink:title" select="@*:title"/>
            </xsl:if>
            <xsl:apply-templates select="node()" mode="#current"/>
        </extptr>
    </xsl:template>


    <!-- class: unitdate -->
    <xsl:template match="unitdate[parent::did]" mode="fonds intermediate lowest">
        <xsl:if test="text() != '-'">
            <unitdate>
                <xsl:attribute name="encodinganalog" select="'3.1.3'"/>
                <xsl:attribute name="era" select="'ce'"/>
                <xsl:attribute name="calendar" select="'gregorian'"/>
                <xsl:call-template name="normalizeDate"/>
                <xsl:apply-templates select="node()" mode="#current"/>
            </unitdate>
        </xsl:if>
    </xsl:template>



    <!-- class: scopecontent[@encodinganalog='327$a'] -->
    <!--xsl:template match="scopecontent" mode="fonds intermediate lowest">
        <scopecontent encodinganalog="summary">
            <xsl:apply-templates select="node() except scopecontent" mode="#current" />
            <xsl:apply-templates select="scopecontent" mode="nested" />
        </scopecontent>
    </xsl:template-->

    <!-- class file series: acqinfo|relatedmaterial|otherfindaid|bibliography -->
    <!--<xsl:template match="acqinfo|relatedmaterial|otherfindaid|bibliography" mode="class file series"/>-->

    <!-- class: accessrestrict|userestrict|controlaccess -->
    <!--<xsl:template match="accessrestrict|userestrict|controlaccess" mode="class"/>-->

    <xsl:template name="daoRoleType">
        <xsl:choose>
            <xsl:when test="@label or @role">
                <xsl:choose>
                    <xsl:when
                        test="@label='thumb' or @role='thumb' or @role='image_thumb' or @*:role='image_thumb'">
                        <xsl:attribute name="xlink:title" select="'thumbnail'"/>
                    </xsl:when>
                    <xsl:when test="@label = 'Document'">
                        <xsl:attribute name="xlink:title" select="@label"/>
                        <xsl:attribute name="xlink:role" select="'TEXT'"/>
                    </xsl:when>
                    <xsl:when test="@label = 'Kaart'">
                        <xsl:attribute name="xlink:title" select="@label"/>
                        <xsl:attribute name="xlink:role" select="'IMAGE'"/>
                    </xsl:when>
                    <xsl:when test="@label = 'Foto'">
                        <xsl:attribute name="xlink:title" select="@label"/>
                        <xsl:attribute name="xlink:role" select="'IMAGE'"/>
                    </xsl:when>
                    <xsl:when test="@label='reference' and daodesc/p/text()">
                        <xsl:attribute name="xlink:title" select="daodesc/p/text()"/>
                        <xsl:attribute name="xlink:role" select="'UNSPECIFIED'"/>
                    </xsl:when>
                    <xsl:when test="@role='reference' and ../daodesc/p/text()">
                        <xsl:attribute name="xlink:title" select="../daodesc/p/text()"/>
                        <xsl:attribute name="xlink:role" select="'UNSPECIFIED'"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:attribute name="xlink:title" select="@label"/>
                        <xsl:attribute name="xlink:role" select="'UNSPECIFIED'"/>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:when>
            <!-- Special condition for IISG from NL -->
            <xsl:when test="@xlink:label">
                <xsl:choose>
                    <xsl:when test="@xlink:label = 'thumbnail'">
                        <xsl:attribute name="xlink:title" select="@xlink:label"/>
                    </xsl:when>
                    <xsl:when test="@xlink:label = 'catalog'">
                        <xsl:attribute name="xlink:title" select="@xlink:label"/>
                    </xsl:when>
                </xsl:choose>
            </xsl:when>
            <xsl:otherwise>
                <xsl:call-template name="daoDigitalType"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <!-- file: did -->
    <xsl:template match="did" mode="fonds intermediate lowest">
        <did>
            <xsl:if test="not(unittitle)">
                <unittitle/>
            </xsl:if>
            <xsl:apply-templates select="node() except abstract" mode="#current"/>
            <xsl:for-each select="following-sibling::dao">
                <!--<dao xlink:href="{@href}"/>-->
                <xsl:call-template name="dao"/>
            </xsl:for-each>
            <xsl:for-each select="../daogrp/daoloc | ../odd/daogrp/daoloc | ../daogrp/dao">
                <dao>
                    <xsl:if test="@href!=''">
                        <xsl:attribute name="xlink:href" select="ape:checkLink(@href)"/>
                    </xsl:if>
                    <xsl:if test="@*:href!=''">
                        <xsl:attribute name="xlink:href" select="ape:checkLink(@*:href)"/>
                    </xsl:if>
                    <xsl:if test="@title!=''">
                        <xsl:attribute name="xlink:title" select="@title"/>
                    </xsl:if>
                    <xsl:if test="@*:title!=''">
                        <xsl:attribute name="xlink:title" select="@*:title"/>
                    </xsl:if>
                    <xsl:if
                        test="not(@title) and not(@*:title) and ../daodesc[@label='reference']/p/text()">
                        <xsl:attribute name="xlink:title"
                                       select="../daodesc[@label='reference']/p/text()"/>
                    </xsl:if>
                    <xsl:if
                        test="not(@title) and not(@*:title) and .[@label='reference']/daodesc/p/text()">
                        <xsl:attribute name="xlink:title"
                                       select=".[@label='reference']/daodesc/p/text()"/>
                    </xsl:if>
                    <xsl:call-template name="daoRoleType"/>
                </dao>
            </xsl:for-each>
            <xsl:for-each select="following-sibling::note">
                <xsl:call-template name="note"/>
            </xsl:for-each>
            <xsl:for-each select="following-sibling::descgrp/p">
                <xsl:call-template name="note"/>
            </xsl:for-each>
            <!--<xsl:variable name="parentNode" select="current()/parent::node()"/>-->
            <!--<xsl:if test="$parentNode/bioghist/p/persname | $parentNode/bioghist/p/list/item/persname | current()/unittitle/persname[text()!=''] | current()/parent::node()/originalsloc/p/persname">-->
            <!--<origination label="pre">-->
            <!--<xsl:for-each select="$parentNode/bioghist/p/persname | $parentNode/bioghist/p/list/item/persname | current()/unittitle/persname | $parentNode/originalsloc/p/persname">-->
            <!--<persname><xsl:value-of select="text()"/></persname>-->
            <!--</xsl:for-each>-->
            <!--</origination>-->
            <!--</xsl:if>-->
            <!--<xsl:if test="$parentNode/bibliography/p/bibref/persname | $parentNode/relatedmaterial/p/persname | $parentNode/scopecontent/p/list/item/persname | $parentNode/scopecontent/p/persname">-->
            <!--<origination label="final">-->
            <!--<xsl:for-each select="$parentNode/bibliography/p/bibref/persname | $parentNode/relatedmaterial/p/persname | $parentNode/scopecontent/p/list/item/persname | $parentNode/scopecontent/p/persname">-->
            <!--<persname><xsl:value-of select="text()"/></persname>-->
            <!--</xsl:for-each>-->
            <!--</origination>-->
            <!--</xsl:if>-->
        </did>
        <xsl:if
            test="not(controlaccess) and (descendant::geogname[parent::item|parent::entry|parent::p|parent::unittitle] or descendant::subject[parent::item|parent::entry|parent::p|parent::unittitle] or descendant::famname[parent::item|parent::entry|parent::p|parent::unittitle] or descendant::persname[parent::item|parent::entry|parent::p|parent::unittitle] or descendant::corpname[parent::item|parent::entry|parent::p|parent::unittitle] or descendant::occupation[parent::item|parent::entry|parent::p|parent::unittitle] or descendant::genreform[parent::item|parent::entry|parent::p|parent::unittitle] or descendant::function[parent::item|parent::entry|parent::p|parent::unittitle] or descendant::title[parent::item|parent::entry|parent::p|parent::unittitle] or descendant::name[parent::item|parent::entry|parent::p|parent::unittitle])">
            <xsl:call-template name="createControlaccess">
                <xsl:with-param name="context" select=".."/>
            </xsl:call-template>
        </xsl:if>

        <xsl:apply-templates select="abstract" mode="#current"/>
    </xsl:template>

    <xsl:template
        match="c/descgrp | c01/descgrp | c02/descgrp | c03/descgrp | c04/descgrp | c05/descgrp | c06/descgrp | c07/descgrp | c08/descgrp | c09/descgrp | c10/descgrp | c11/descgrp | c12/descgrp"
        mode="fonds intermediate lowest">
        <xsl:apply-templates mode="#current"/>
    </xsl:template>
    <xsl:template match="descgrp/p" mode="fonds intermediate lowest"/>
    <!--<note encodinganalog="3.6.1">-->
    <!--<xsl:apply-templates mode="#current"/>-->
    <!--</note>-->
    <!--</xsl:template>-->

    <!--Nothing because already used earlier-->
    <xsl:template
        match="c/dao | c01/dao | c02/dao | c03/dao | c04/dao | c05/dao | c06/dao | c07/dao | c08/dao | c09/dao | c10/dao | c11/dao | c12/dao"
        mode="fonds intermediate lowest"/>
    <!--<xsl:template match="scopecontent[not(ancestor::did)]" mode="lowest" />-->
    <xsl:template
        match="c/daogrp | c01/daogrp | c02/daogrp | c03/daogrp | c04/daogrp | c05/daogrp | c06/daogrp | c07/daogrp | c08/daogrp | c09/daogrp | c10/daogrp | c11/daogrp | c12/daogrp"
        mode="fonds intermediate lowest">
        <xsl:apply-templates select="node()" mode="#current"/>
    </xsl:template>
    <xsl:template
        match="c/daogrp/resource | c01/daogrp/resource | c02/daogrp/resource | c03/daogrp/resource | c04/daogrp/resource | c05/daogrp/resource | c06/daogrp/resource | c07/daogrp/resource | c08/daogrp/resource | c09/daogrp/resource | c10/daogrp/resource | c11/daogrp/resource | c12/daogrp/resource"
        mode="fonds intermediate lowest"/>
    <xsl:template
        match="c/daogrp/arc | c01/daogrp/arc | c02/daogrp/arc | c03/daogrp/arc | c04/daogrp/arc | c05/daogrp/arc | c06/daogrp/arc | c07/daogrp/arc | c08/daogrp/arc | c09/daogrp/arc | c10/daogrp/arc | c11/daogrp/arc | c12/daogrp/arc"
        mode="fonds intermediate lowest"/>
    <xsl:template
        match="c/daogrp/daoloc | c01/daogrp/daoloc | c02/daogrp/daoloc | c03/daogrp/daoloc | c04/daogrp/daoloc | c05/daogrp/daoloc | c06/daogrp/daoloc | c07/daogrp/daoloc | c08/daogrp/daoloc | c09/daogrp/daoloc | c10/daogrp/daoloc | c11/daogrp/daoloc | c12/daogrp/daoloc"
        mode="fonds intermediate lowest"/>
    <xsl:template
        match="c/daogrp/dao | c01/daogrp/dao | c02/daogrp/dao | c03/daogrp/dao | c04/daogrp/dao | c05/daogrp/dao | c06/daogrp/dao | c07/daogrp/dao | c08/daogrp/dao | c09/daogrp/dao | c10/daogrp/dao | c11/daogrp/dao | c12/daogrp/dao"
        mode="fonds intermediate lowest"/>
    <xsl:template
        match="c/odd/daogrp | c01/odd/daogrp | c02/odd/daogrp | c03/odd/daogrp | c04/odd/daogrp | c05/odd/daogrp | c06/odd/daogrp | c07/odd/daogrp | c08/odd/daogrp | c09/odd/daogrp | c10/odd/daogrp | c11/odd/daogrp | c12/odd/daogrp"
        mode="fonds intermediate lowest">
        <xsl:apply-templates select="node()" mode="#current"/>
    </xsl:template>
    <xsl:template
        match="c/odd/daogrp/resource | c01/odd/daogrp/resource | c02/odd/daogrp/resource | c03/odd/daogrp/resource | c04/odd/daogrp/resource | c05/odd/daogrp/resource | c06/odd/daogrp/resource | c07/odd/daogrp/resource | c08/odd/daogrp/resource | c09/odd/daogrp/resource | c10/odd/daogrp/resource | c11/odd/daogrp/resource | c12/odd/daogrp/resource"
        mode="fonds intermediate lowest"/>
    <xsl:template
        match="c/odd/daogrp/arc | c01/odd/daogrp/arc | c02/odd/daogrp/arc | c03/odd/daogrp/arc | c04/odd/daogrp/arc | c05/odd/daogrp/arc | c06/odd/daogrp/arc | c07/odd/daogrp/arc | c08/odd/daogrp/arc | c09/odd/daogrp/arc | c10/odd/daogrp/arc | c11/odd/daogrp/arc | c12/odd/daogrp/arc"
        mode="fonds intermediate lowest"/>
    <xsl:template
        match="c/odd/daogrp/daoloc | c01/odd/daogrp/daoloc | c02/odd/daogrp/daoloc | c03/odd/daogrp/daoloc | c04/odd/daogrp/daoloc | c05/odd/daogrp/daoloc | c06/odd/daogrp/daoloc | c07/odd/daogrp/daoloc | c08/odd/daogrp/daoloc | c09/odd/daogrp/daoloc | c10/odd/daogrp/daoloc | c11/odd/daogrp/daoloc | c12/odd/daogrp/daoloc"
        mode="fonds intermediate lowest"/>
    <xsl:template
        match="c/note | c01/note | c02/note | c03/note | c04/note | c05/note | c06/note | c07/note | c08/note | c09/note | c10/note | c11/note | c12/note"
        mode="fonds intermediate lowest"/>

    <xsl:template match="did/dao" name="dao" mode="fonds intermediate lowest">
        <dao>
            <xsl:choose>
                <xsl:when test="(@xlink:href != '') and (@href != '')">
                    <xsl:attribute name="xlink:href" select="ape:checkLink(@xlink:href)"/>
                </xsl:when>
                <xsl:when test="@*:href != ''">
                    <xsl:attribute name="xlink:href" select="ape:checkLink(@*:href)"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:if test="@href != ''">
                        <xsl:attribute name="xlink:href" select="ape:checkLink(@href)"/>
                    </xsl:if>
                </xsl:otherwise>
            </xsl:choose>
            <xsl:if test="@title!=''">
                <xsl:attribute name="xlink:title" select="@title"/>
            </xsl:if>
            <xsl:if test="@*:title!=''">
                <xsl:attribute name="xlink:title" select="@*:title"/>
            </xsl:if>
            <xsl:if test="not(@title) and not(@*:title) and daodesc/p/text()">
                <xsl:attribute name="xlink:title" select="daodesc/p/text()"/>
            </xsl:if>
            <xsl:call-template name="daoDigitalType"/>
        </dao>
    </xsl:template>

    <xsl:template name="daoDigitalType">
        <xsl:choose>
            <xsl:when test="$useDefaultRoleType = 'true'">
                <xsl:attribute name="xlink:role" select="$defaultRoleType"/>
            </xsl:when>
            <xsl:when test="$useDefaultRoleType = 'false' and @role">
                <xsl:attribute name="xlink:role" select="@role"/>
            </xsl:when>
            <xsl:when test="$useDefaultRoleType = 'false' and @*:role">
                <xsl:attribute name="xlink:role" select="@*:role"/>
            </xsl:when>
            <xsl:when test="$useDefaultRoleType = 'false'">
                <xsl:attribute name="xlink:role" select="$defaultRoleType"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:attribute name="xlink:role" select="'UNSPECIFIED'"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template match="did/daogrp" mode="fonds intermediate lowest">
        <xsl:apply-templates mode="#current"/>
    </xsl:template>

    <xsl:template match="did/daogrp/daoloc | did/daoloc" mode="fonds intermediate lowest">
        <xsl:choose>
            <xsl:when test="@actuate='user'">
                <dao>
                    <xsl:attribute name="xlink:href" select="./text()"/>
                </dao>
            </xsl:when>
            <xsl:when test="@label='reference' or @label='thumb' or @linktype='locator'">
                <dao>
                    <xsl:if test="@href!=''">
                        <xsl:attribute name="xlink:href" select="ape:checkLink(@href)"/>
                    </xsl:if>
                    <xsl:if test="@*:href!=''">
                        <xsl:attribute name="xlink:href" select="ape:checkLink(@*:href)"/>
                    </xsl:if>
                    <xsl:call-template name="daoRoleType"/>
                </dao>
            </xsl:when>
            <xsl:when test="@xlink:href">
                <xsl:choose>
                    <!-- Special condition for IISG from NL -->
                    <xsl:when test="@xlink:label = 'pdf' or @xlink:label = 'mets'">
                        <xsl:call-template name="excludeElement"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <dao>
                            <xsl:attribute name="xlink:href" select="ape:checkLink(@xlink:href)"/>
                            <xsl:if test="@xlink:title">
                                <xsl:attribute name="xlink:title" select="@xlink:title"/>
                            </xsl:if>
                            <xsl:call-template name="daoRoleType"/>
                        </dao>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:when>
            <xsl:when test="@href">
                <dao>
                    <xsl:attribute name="xlink:href" select="ape:checkLink(@href)"/>
                    <xsl:if test="@title">
                        <xsl:attribute name="xlink:title" select="@title"/>
                    </xsl:if>
                    <xsl:call-template name="daoRoleType"/>
                </dao>
            </xsl:when>
            <xsl:otherwise>
                <xsl:call-template name="excludeElement"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template match="did/daogrp/daodesc" mode="fonds intermediate lowest"/>
    <xsl:template match="did/daogrp/resource" mode="fonds intermediate lowest"/>
    <xsl:template match="did/daogrp/arc" mode="fonds intermediate lowest"/>

    <xsl:template match="did/abstract" mode="fonds">
        <scopecontent encodinganalog="summary">
            <xsl:if test="@type">
                <head>
                    <xsl:value-of select="@type"/>
                </head>
            </xsl:if>
            <p>
                <xsl:apply-templates select="node()" mode="#current"/>
            </p>
        </scopecontent>
    </xsl:template>

    <xsl:template match="did/abstract" mode="intermediate lowest">
        <scopecontent encodinganalog="summary">
            <xsl:if test="@type">
                <head>
                    <xsl:value-of select="@type"/>
                </head>
            </xsl:if>
            <p>
                <xsl:apply-templates select="node()" mode="#current"/>
            </p>
        </scopecontent>
    </xsl:template>

    <xsl:template match="did/container" mode="copy fonds intermediate lowest">
        <container>
            <xsl:if test="@type">
                <xsl:attribute name="type" select="@type"/>
            </xsl:if>
            <xsl:if test="@parent">
                <xsl:attribute name="parent" select="@parent"/>
            </xsl:if>
            <xsl:apply-templates select="node()" mode="#current"/>
        </container>
    </xsl:template>

    <!-- copy: custodhist -->
    <xsl:template match="custodhist" mode="copy fonds intermediate lowest">
        <xsl:if test="count(child::*) != 0 or normalize-space(text()) != ''">
            <custodhist encodinganalog="3.2.3">
                <xsl:choose>
                    <xsl:when test="normalize-space(text()) != ''">
                        <p>
                            <xsl:apply-templates mode="#current"/>
                        </p>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:apply-templates
                            select="node()[not(name()='acqinfo' or name()='custodhist')]"
                            mode="#current"/>
                        <!--except acqinfo?-->
                        <xsl:apply-templates select="custodhist" mode="nested"/>
                    </xsl:otherwise>
                </xsl:choose>
            </custodhist>
        </xsl:if>
    </xsl:template>

    <xsl:template match="custodhist/date" mode="copy fonds intermediate lowest">
        <xsl:choose>
            <xsl:when test="preceding-sibling::*[name()='p']">
                <p>
                    <xsl:apply-templates mode="#current"/>
                </p>
            </xsl:when>
            <xsl:otherwise>
                <xsl:text> </xsl:text>
                <xsl:apply-templates mode="#current"/>
                <xsl:text> </xsl:text>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template match="custodhist[ancestor::custodhist]" mode="copy fonds intermediate lowest"/>

    <xsl:template match="custodhist[ancestor::custodhist]" mode="nested">
        <xsl:apply-templates select="node()" mode="nested"/>
    </xsl:template>

    <xsl:template match="index" mode="copy fonds intermediate lowest">
        <xsl:choose>
            <xsl:when test="../controlaccess">
                <xsl:call-template name="excludeElement"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:call-template name="controlaccess"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <!-- all: unittitle/unitdate
    <xsl:template match="unittitle/unitdate" mode="#all">
        <xsl:text> </xsl:text>
        <xsl:apply-templates select="node()" mode="#current"/>
        <xsl:text> </xsl:text>
    </xsl:template>-->

    <!-- all: unittitle/archref -->
    <xsl:template match="unittitle/archref" mode="#all">
        <xsl:apply-templates select="node()" mode="#current"/>
    </xsl:template>

    <!-- all: emph/emph -->
    <xsl:template match="emph/emph" mode="#all">
        <xsl:apply-templates select="node()" mode="#current"/>
    </xsl:template>

    <!--
      normalize date
      takes as input: DD.MM.YYYY
      outputs: YYYY-MM-DD
    -->
    <xsl:template name="normalizeDate">
        <xsl:choose>
            <xsl:when test="@normal">
                <xsl:variable name="normal">
                    <xsl:value-of select="ape:normalizeDate(normalize-space(@normal))"/>
                </xsl:variable>
                <xsl:choose>
                    <xsl:when test="normalize-space($normal)">
                        <xsl:attribute name="normal">
                            <xsl:value-of select="$normal"/>
                        </xsl:attribute>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:variable name="normal_2">
                            <xsl:value-of select="ape:normalizeDate(normalize-space(.))"/>
                        </xsl:variable>
                        <xsl:if test="normalize-space($normal_2)">
                            <xsl:attribute name="normal">
                                <xsl:value-of select="$normal_2"/>
                            </xsl:attribute>
                        </xsl:if>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:when>
            <xsl:otherwise>
                <xsl:variable name="normal">
                    <xsl:value-of select="ape:normalizeDate(normalize-space(.))"/>
                </xsl:variable>
                <xsl:if test="normalize-space($normal)">
                    <xsl:attribute name="normal">
                        <xsl:value-of select="$normal"/>
                    </xsl:attribute>
                </xsl:if>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <!--todo: WARNING!! Those are just taken away and are NOT provided to the user as missing elements-->
    <xsl:template match="arrangement | originalsloc | separatedmaterial | acqinfo | prefercite"
                  mode="intermediate lowest"/>

    <!-- c-level -->
    <xsl:template match="c | c01 | c02 | c03 | c04 | c05 | c06 | c07 | c08 | c09 | c10 | c11 | c12"
                  mode="copy fonds intermediate lowest">
        <!--Counter-->
        <xsl:value-of select="ape:counterclevel()"/>
        <!---->
        <xsl:variable name="level">
            <xsl:choose>
                <xsl:when test="@level='item'">
                    <xsl:choose>
                        <xsl:when test="(child::c[@level='item'])">
                            <!-- or (following-sibling::*/child::*/@level='item') or (preceding-sibling::*/child::*/@level='item')">-->
                            <xsl:value-of select="'file'"/>
                        </xsl:when>
                        <xsl:when
                            test="child::c[not(@level)] or child::c[@otherlevel='partie-de-pièce']">
                            <xsl:value-of select="'file'"/>
                        </xsl:when>
                        <xsl:when test="child::c[@level='file']">
                            <xsl:value-of select="'subseries'"/>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:value-of select="'item'"/>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:when>
                <xsl:when test="@level='file'">
                    <xsl:choose>
                        <xsl:when
                            test="child::c[@level='file'] or child::c[@otherlevel='sous-dossier'] or (child::c[@otherlevel='subfile' or @otherlevel='item'] and child::c/child::c[@level='item'])">
                            <!-- or (following-sibling::c/child::c[@otherlevel='subfile' or @otherlevel='item'] and following-sibling::c/child::c/child::c[@level='item']) or (preceding-sibling::c/child::c[@otherlevel='subfile' or @otherlevel='item'] and preceding-sibling::c/child::c/child::c[@level='item'])">-->
                            <xsl:value-of select="'subseries'"/>
                        </xsl:when>
                        <xsl:when
                            test="parent::c[@otherlevel='subfile'] and parent::c/parent::c[@otherlevel='filegrp']">
                            <xsl:value-of select="'item'"/>
                        </xsl:when>
                        <xsl:when test="parent::c[not(@level)]">
                            <xsl:value-of select="'subseries'"/>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:value-of select="'file'"/>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:when>
                <xsl:when
                    test="@level='subseries' or @level='recordgrp' or @level='subgrp' or (@level='series' and //eadid/@countrycode = 'DE')">
                    <xsl:choose>
                        <xsl:when test="child::c[@level='series']">
                            <xsl:value-of select="'series'"/>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:value-of select="'subseries'"/>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:when>
                <xsl:when
                    test="(@level='series' and (//eadid/@countrycode != 'DE' or not(//eadid/@countrycode))) or @level='subfonds' or @level='class' or not(@level)">
                    <xsl:choose>
                        <xsl:when
                            test="parent::c[@otherlevel='filegrp'] or (not(@level) and parent::c[@level='subseries'])">
                            <xsl:value-of select="'file'"/>
                        </xsl:when>
                        <xsl:when test="child::c[@level='item']">
                            <!-- or following-sibling::c/child::c[@level='item'] or preceding-sibling::c/child::c[@level='item']">-->
                            <xsl:value-of select="'file'"/>
                        </xsl:when>
                        <xsl:when test="@level='subfonds' and parent::c[@level='subseries']">
                            <xsl:value-of select="'subseries'"/>
                        </xsl:when>
                        <xsl:when test="not(@level) and parent::c[@level='file' or @level='item']">
                            <xsl:value-of select="'item'"/>
                        </xsl:when>
                        <xsl:when test="parent::c[@level='file'] and not(child::c)">
                            <xsl:value-of select="'item'"/>
                        </xsl:when>
                        <xsl:when test="parent::c[@level='file'] and child::c">
                            <xsl:value-of select="'file'"/>
                        </xsl:when>
                        <xsl:when
                            test="parent::c[@otherlevel='groupe-de-fonds' or @otherlevel='groupe_de_fonds' or not(@level)]">
                            <xsl:value-of select="'subseries'"/>
                        </xsl:when>
                        <xsl:when
                            test="not(@level) and (parent::c[@otherlevel='sous-sous-serie-organique' or @otherlevel='recordgrp'] or child::c[@otherlevel='sous-sous-serie-organique'] or not(child::c))">
                            <xsl:value-of select="'subseries'"/>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:value-of select="'series'"/>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:when>
                <xsl:when test="@level='fonds' or @level='collection'">
                    <xsl:choose>
                        <xsl:when
                            test="parent::c[@otherlevel='groupe-de-fonds' or @otherlevel='groupe_de_fonds' or @otherlevel='groupe-de-fonds-et-de-collections' or not(@level)]">
                            <xsl:value-of select="'series'"/>
                        </xsl:when>
                        <xsl:when
                            test="parent::c[@level='fonds']/parent::c[not(@level) or @otherlevel='groupe-de-fonds']">
                            <xsl:value-of select="'series'"/>
                        </xsl:when>
                        <xsl:when test="parent::c[@level='subseries']">
                            <xsl:value-of select="'subseries'"/>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:value-of select="'fonds'"/>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:when>
                <xsl:when test="@level='otherlevel'">
                    <xsl:choose>
                        <xsl:when
                            test="@otherlevel='filegrp' or @otherlevel=' filegrp' or @otherlevel='filegroup' or @otherlevel='file' or @otherlevel='subseries' or @otherlevel='subsubseries' or @otherlevel='sous-serie' or @otherlevel='sous-sous-serie' or @otherlevel='sous-sous-sous-serie' or @otherlevel='sous-sous-sous-sous-série' or @otherlevel='subsubsubseries' or @otherlevel='subsubsusbseries' or @otherlevel='subsubsubsubseries' or @otherlevel='sous-sous-série' or @otherlevel='sous-sous-sous-série' or @otherlevel='partie-de-subgp' or @otherlevel='partie-de-subgrp' or @otherlevel='partie-de-sbgrp' or @otherlevel='subsubsubsubsubseries' or @otherlevel='sous-série' or @otherlevel='sous-dossiers' or @otherlevel='sous-sous-serie-organique' or @otherlevel='sous-sous-sous-serie-organique' or @otherlevel='sub-subseries' or @otherlevel='box'">
                            <xsl:value-of select="'subseries'"/>
                        </xsl:when>
                        <xsl:when
                            test="((@otherlevel='groupe-de-fonds' or @otherlevel='groupe_de_fonds') and not(parent::c[not(@level) or @level='file' or @otherlevel='groupe-de-fonds' or @otherlevel='groupe_de_fonds'])) or @otherlevel='SF' or @otherlevel='sous-fonds' or @otherlevel='sous-sous-fonds'">
                            <xsl:value-of select="'fonds'"/>
                        </xsl:when>
                        <xsl:when
                            test="@otherlevel='groupe-de-fonds-et-de-collections' or @otherlevel='groupe-de-series' or @otherlevel='groupe-de-fonds-et-de-sous-fonds' or @otherlevel='SC' or @otherlevel='SSC' or @otherlevel='SSSC' or @otherlevel='SR' or @otherlevel='SSR' or @otherlevel='SSSR' or @otherlevel='partie-de-fonds' or @otherlevel='partie-de-sous-fonds' or @otherlevel='sous-collection' or @otherlevel='article' or @otherlevel='SubCollection' or @otherlevel='sub-subfonds' or @otherlevel='sub sub fonds' or @otherlevel='sub-sub-subfonds' or @otherlevel='sub sub sub fonds' or @otherlevel='Section' or @otherlevel='SubSection'">
                            <xsl:value-of select="'series'"/>
                        </xsl:when>
                        <xsl:when
                            test="@otherlevel='sous-sous-serie-organique' or @otherlevel='DC' or ((@otherlevel='groupe-de-fonds' or @otherlevel='groupe_de_fonds') and parent::c[not(@level) or @otherlevel='groupe-de-fonds' or @otherlevel='groupe_de_fonds'])">
                            <xsl:value-of select="'series'"/>
                        </xsl:when>
                        <xsl:when
                            test="@otherlevel='UI' or @otherlevel='volym' or @otherlevel='karta' or (@otherlevel='sous-dossier' and (child::c[@otherlevel='sous-sous-dossier'] or child::dsc/child::c[@level='item'])) or @otherlevel='sous-sous-dossier_factice' or @otherlevel='sous-sous-groupe' or @otherlevel='groupe-de-pièces' or @otherlevel='piece'">
                            <xsl:value-of select="'file'"/>
                        </xsl:when>
                        <xsl:when
                            test="@otherlevel='D' or ((@otherlevel='groupe-de-fonds' or @otherlevel='groupe_de_fonds') and parent::c[@level='file']) or @otherlevel='partie-de-pièce' or @otherlevel='partie-de-dossier' or @otherlevel='sous-dossier' or @otherlevel='sous-sous-dossier' or @otherlevel='ss_ss_fonds' or @otherlevel='Piece'">
                            <xsl:value-of select="'item'"/>
                        </xsl:when>
                        <xsl:when test="@otherlevel='subfile'">
                            <xsl:choose>
                                <xsl:when
                                    test="child::c[@level='file' or @level='item' or @otherlevel='subfile']">
                                    <!-- or following-sibling::c/child::c[@level='file' or @level='item' or @otherlevel='subfile'] or preceding-sibling::c/child::c[@level='file' or @level='item' or @otherlevel='subfile']">-->
                                    <xsl:value-of select="'file'"/>
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:value-of select="'item'"/>
                                </xsl:otherwise>
                            </xsl:choose>
                        </xsl:when>
                        <xsl:when test="@otherlevel='arkusz'">
                            <xsl:value-of select="'item'"/>
                        </xsl:when>
                        <xsl:when test="not(@otherlevel) and child::c[@level='series']">
                            <xsl:value-of select="'series'"/>
                        </xsl:when>
                        <xsl:when test="not(@otherlevel)">
                            <xsl:value-of select="'subseries'"/>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:message select="@otherlevel"> - WARNING! We don't know this c
                                otherlevel attribute</xsl:message>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:message select="@level"> - WARNING! We don't know this c level
                        attribute</xsl:message>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <xsl:choose>
            <xsl:when test="@level='collection' and did/unitid='' and did/unittitle=''">
                <xsl:apply-templates select="child::*[not(name()='did')]" mode="copy"/>
            </xsl:when>
            <xsl:otherwise>
                <c level="{$level}">
                    <!--<xsl:param name="levelprecedent" select="$level" />-->
                    <!--xsl:if test="normalize-space(@id)">
                        <xsl:attribute name="id" select="normalize-space(@id)"/>
                    </xsl:if-->
                    <xsl:attribute name="id">
                        <xsl:choose>
                            <xsl:when test="normalize-space(@id)">
                                <xsl:value-of select="normalize-space(@id)"/>
                            </xsl:when>
                            <xsl:when test="normalize-space(child::*[name()='did']/@id)">
                                <xsl:value-of select="normalize-space(child::*[name()='did']/@id)"/>
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:value-of select="generate-id()"/>
                            </xsl:otherwise>
                        </xsl:choose>
                    </xsl:attribute>
                    <xsl:if test="$level='item'">
                        <xsl:attribute name="encodinganalog" select="'3.1.4'"/>
                        <xsl:apply-templates mode="lowest"/>
                    </xsl:if>
                    <xsl:if test="$level='file'">
                        <xsl:attribute name="encodinganalog" select="'3.1.4'"/>
                        <xsl:apply-templates mode="lowest"/>
                    </xsl:if>
                    <xsl:if test="$level='subseries'">
                        <xsl:attribute name="encodinganalog" select="'3.1.4'"/>
                        <xsl:apply-templates mode="intermediate"/>
                    </xsl:if>
                    <xsl:if test="$level='series'">
                        <xsl:attribute name="encodinganalog" select="'3.1.4'"/>
                        <xsl:apply-templates mode="intermediate"/>
                    </xsl:if>
                    <xsl:if test="$level='fonds'">
                        <xsl:attribute name="encodinganalog" select="'3.1.4'"/>
                        <xsl:apply-templates mode="fonds"/>
                    </xsl:if>
                   <!--<xsl:if test="not(controlaccess) and not(did/controlaccess) and not(index) and not(did/index)">-->
                    <!--<xsl:call-template name="createControlaccess">-->
                    <!--<xsl:with-param name="context" select="."/>-->
                    <!--</xsl:call-template>-->
                    <!--</xsl:if>-->
                </c>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

</xsl:stylesheet>
