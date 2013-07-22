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
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format"
                xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                xmlns:none="none"
                xmlns:ape="http://www.archivesportaleurope.net/functions"
                xmlns:xlink="http://www.w3.org/1999/xlink"
                xsi:schemaLocation="urn:isbn:1-931666-22-9 http://www.loc.gov/ead/ead.xsd"
                xpath-default-namespace="urn:isbn:1-931666-22-9"
                exclude-result-prefixes="xsl fo xs none ape">

    <xsl:param name="loclanguage" select="'xsl/languages.xml'"/>
    <xsl:variable name="langfile" select="document($loclanguage)"/>

    <xsl:param name="eadiddefault" select="''"/>

    <xsl:param name="langusage" select="''"/>
    <xsl:param name="langmaterial" select="''"/>
    <xsl:param name="addressline" select="''"/>
    <xsl:param name="publisher" select="''"/>
    <xsl:param name="author" select="''"/>
    <xsl:param name="repository" select="''"/>
    <xsl:param name="prefercite" select="''"/>
    <xsl:param name="mainagencycode" select="'SI-NAS'"/>
    <xsl:param name="countrycode" select="'SI'"/>
    <xsl:param name="versionnb" select="''"/>
    <xsl:param name="eadidmissing" select="''"/>
    <xsl:param name="useXSD10" select="'false'"/>

    <xsl:param name="url" select="''"/>
    <xsl:param name="provider" select="''"/>

    <xsl:output indent="yes" method="xml" />
    <xsl:strip-space elements="*"/>
    <!-- / -->

    <xsl:template match="/">
        <xsl:apply-templates select="node()" mode="top"/>
    </xsl:template>

    <!--#all: copy fonds intermediate lowest nested-->
    <!-- Elements unknown -->
    <xsl:template match="*" name="excludeElement" mode="#all">
        <xsl:variable name="excludedElement">
            <xsl:if test="name(../../../../../../../..) != ''">
                <xsl:value-of select="name(../../../../../../../..)"/><xsl:if test="name(../../../../../../../..)='c'">@<xsl:value-of select="../../../../../../../../@level" /></xsl:if><xsl:text>/</xsl:text>
            </xsl:if>
            <xsl:if test="name(../../../../../../..) != ''">
                <xsl:value-of select="name(../../../../../../..)"/><xsl:if test="name(../../../../../../..)='c'">@<xsl:value-of select="../../../../../../../@level" /></xsl:if><xsl:text>/</xsl:text>
            </xsl:if>
            <xsl:if test="name(../../../../../..) != ''">
                <xsl:value-of select="name(../../../../../..)"/><xsl:if test="name(../../../../../..)='c'">@<xsl:value-of select="../../../../../../@level" /></xsl:if><xsl:text>/</xsl:text>
            </xsl:if>
            <xsl:if test="name(../../../../..) != ''">
                <xsl:value-of select="name(../../../../..)"/><xsl:if test="name(../../../../..)='c'">@<xsl:value-of select="../../../../../@level" /></xsl:if><xsl:text>/</xsl:text>
            </xsl:if>
            <xsl:if test="name(../../../..) != ''">
                <xsl:value-of select="name(../../../..)"/><xsl:if test="name(../../../..)='c'">@<xsl:value-of select="../../../../@level" /></xsl:if><xsl:text>/</xsl:text>
            </xsl:if>
            <xsl:if test="name(../../..) != ''">
                <xsl:value-of select="name(../../..)"/><xsl:if test="name(../../..)='c'">@<xsl:value-of select="../../../@level" /></xsl:if><xsl:text>/</xsl:text>
            </xsl:if>
            <xsl:value-of select="name(../..)"/><xsl:if test="name(../..)='c'">@<xsl:value-of select="../../@level" /></xsl:if><xsl:text>/</xsl:text><xsl:value-of select="name(..)"/><xsl:if test="name(..)='c'">@<xsl:value-of select="../@level" /></xsl:if><xsl:text>/</xsl:text><xsl:value-of select="name(.)"/><xsl:if test="name(.)='c'">@<xsl:value-of select="@level" /></xsl:if>
        </xsl:variable>
        <xsl:message select="normalize-space($excludedElement)" />
    </xsl:template>

    <!--
         copy all text nodes
         node() and text() have the same priority in XSLT 1.0 and 2.0
         to avoid "Recoverable error: XTRE0540: Ambiguous rule match for..."
         it is better to provide a priority
       -->
    <xsl:template match="text()" mode="#all" priority="2">
        <xsl:choose>
            <xsl:when test="contains(., '&#xa;')">
                <xsl:value-of select="translate(normalize-space(.), '&#xa;', ' ')"/>
            </xsl:when>
            <xsl:otherwise>
                <!--xsl:if test="preceding-sibling::node()[1][self::node()]"><xsl:text> </xsl:text></xsl:if--><xsl:value-of select="."/><!--xsl:if test="following-sibling::node()[1][self::node()]"><xsl:text> </xsl:text></xsl:if-->
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <!-- ead -->
    <xsl:template match="*:ExportRoot" name="ead" mode="top">
        <xsl:choose>
            <xsl:when test="$useXSD10 = 'true'">
                <ead xmlns="urn:isbn:1-931666-22-9" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="urn:isbn:1-931666-22-9 http://www.archivesportaleurope.net/Portal/profiles/apeEAD_XSD1.0.xsd http://www.w3.org/1999/xlink http://www.loc.gov/standards/xlink/xlink.xsd" audience="external">
                    <eadheader countryencoding="iso3166-1" dateencoding="iso8601" langencoding="iso639-2b" repositoryencoding="iso15511" scriptencoding="iso15924" relatedencoding="MARC21">
                        <eadid>
                            <xsl:if test="normalize-space($mainagencycode)">
                                <xsl:attribute name="mainagencycode" select="$mainagencycode"/>
                            </xsl:if>
                            <xsl:if test="normalize-space($countrycode)">
                                <xsl:attribute name="countrycode" select="$countrycode"/>
                            </xsl:if>
                            <xsl:attribute name="identifier">
                                <xsl:value-of select="$countrycode"/>_<xsl:value-of select="$mainagencycode"/>
                            </xsl:attribute>
                            <xsl:value-of select="$eadidmissing"/>
                        </eadid>
                        <filedesc>
                            <titlestmt>
                                <titleproper>SOME TITLE</titleproper>
                            </titlestmt>
                        </filedesc>
                        <revisiondesc>
                            <change>
                                <date />
                                <item>Converted_apeEAD_version_<xsl:value-of select="$versionnb"/></item>
                            </change>
                        </revisiondesc>
	  	            </eadheader>
                    <archdesc level="fonds">
                        <did>
                            <unitid/>
                        </did>
                        <dsc type="othertype">
                            <xsl:apply-templates select="node()" mode="copy"/>
                        </dsc>
                    </archdesc>
                </ead>
            </xsl:when>
            <xsl:otherwise>
                <ead xmlns="urn:isbn:1-931666-22-9" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="urn:isbn:1-931666-22-9 http://www.archivesportaleurope.net/Portal/profiles/apeEAD.xsd http://www.w3.org/1999/xlink http://www.loc.gov/standards/xlink/xlink.xsd" audience="external">
                    <eadheader countryencoding="iso3166-1" dateencoding="iso8601" langencoding="iso639-2b" repositoryencoding="iso15511" scriptencoding="iso15924" relatedencoding="MARC21">
                        <eadid>
                            <xsl:if test="normalize-space($mainagencycode)">
                                <xsl:attribute name="mainagencycode" select="$mainagencycode"/>
                            </xsl:if>
                            <xsl:if test="normalize-space($countrycode)">
                                <xsl:attribute name="countrycode" select="$countrycode"/>
                            </xsl:if>
                            <xsl:attribute name="identifier">
                                <xsl:value-of select="$countrycode"/>_<xsl:value-of select="$mainagencycode"/>
                            </xsl:attribute>
                            <xsl:value-of select="$eadidmissing"/>
                        </eadid>
                        <filedesc>
                            <titlestmt>
                                <titleproper>SOME TITLE</titleproper>
                            </titlestmt>
                        </filedesc>
                        <revisiondesc>
                            <change>
                                <date />
                                <item>Converted_apeEAD_version_<xsl:value-of select="$versionnb"/></item>
                            </change>
                        </revisiondesc>
	  	            </eadheader>
                    <archdesc level="fonds">
                        <did>
                            <unitid/>
                        </did>
                        <dsc type="othertype">
                            <xsl:apply-templates select="node()" mode="copy"/>
                        </dsc>
                    </archdesc>
                </ead>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <!-- c -->
    <xsl:template match="*:Record" mode="copy">
        <c>
            <xsl:variable name="myLevel">
                <xsl:if test="@Level='Fond'">fonds</xsl:if>
                <xsl:if test="@Level='Podfond'">series</xsl:if>
                <xsl:if test="@Level='Serija'">series</xsl:if>
                <xsl:if test="@Level='Dokument'">item</xsl:if>
                <xsl:if test="@Level='Slika'">item</xsl:if>
                <xsl:if test="@Level='Združeni dokumenti'">file</xsl:if>
            </xsl:variable>
            <xsl:attribute name="level" select="$myLevel"/>
            <did>
                <unitid>
                    <xsl:value-of select="*:DetailData/*:DataElement[@ElementId='2']/*:ElementValue/*:TextValue/text()"/>
                </unitid>
                <unittitle>
                    <xsl:value-of select="*:DetailData/*:DataElement[@ElementId='1']/*:ElementValue/*:TextValue/text()"/>
                </unittitle>
                <unitdate>
                    <xsl:value-of select="*:DetailData/*:DataElement[@ElementId='7']/*:ElementValue/*:DateRange/*:TextRepresentation/text()"/>
                </unitdate>
            </did>
            <xsl:if test="*:DetailData/*:DataElement[@ElementId='10084']/*:ElementValue/*:TextValue/text()">
                <scopecontent>
                    <p>
                        <xsl:value-of select="*:DetailData/*:DataElement[@ElementId='10084']/*:ElementValue/*:TextValue/text()"/>
                    </p>
                </scopecontent>
            </xsl:if>
            <xsl:apply-templates mode="copy"/> 
        </c>
	</xsl:template>



</xsl:stylesheet>
