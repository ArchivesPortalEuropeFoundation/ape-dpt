<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns="urn:isbn:1-931666-22-9"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format"
                xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:none="none"
                xmlns:ape="http://www.archivesportaleurope.net/functions"
                xmlns:xlink="http://www.w3.org/1999/xlink"
                xsi:schemaLocation="urn:isbn:1-931666-22-9 http://www.loc.gov/ead/ead.xsd"
                xpath-default-namespace="urn:isbn:1-931666-22-9" exclude-result-prefixes="xsl fo xs none ape">

    <xsl:import href="system/default-apeEAD.xsl"/>

    <xsl:template match="archdesc/did/unitid" mode="copy">
        <unitid encodinganalog="3.1.1" type="call number">
            <xsl:value-of select="//eadid/text()"/>
        </unitid>
    </xsl:template>

    <xsl:template match="unitid" mode="level">
        <xsl:choose>
            <xsl:when test="$countrycode='NL' and ../../@otherlevel='subfile'">
                <xsl:apply-templates select="../../../did/unitid" mode="#current"/>
            </xsl:when>
            <xsl:when test="@audience='internal' and @type='handle'"/>
            <xsl:when
                    test="(@type=&quot;cote d'archives&quot; or @type='call number' or @type='ABS' or @type='bestellnummer' or @type='Bestellnummer' or @type='series_code' or @type='reference' or @type='Sygnatura' or @type='REFERENCE_CODE' or @type='cote-de-consultation' or @type='cote-groupee' or @type='identifiant' or @type='cote' or @type='persistent' or (not(@type))) and (text()[string-length(normalize-space(.)) ge 1] or exists(extptr))">
                <!-- and not(preceding-sibling::unitid) and not(following-sibling::unitid)-->
                <unitid>
                    <xsl:attribute name="type" select="'call number'"/>
                    <xsl:attribute name="encodinganalog" select="'3.1.1'"/>
                    <xsl:if test="extptr">
                        <xsl:apply-templates select="extptr" mode="#current"/>
                    </xsl:if>
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
                        <xsl:when test="starts-with(., //eadid/text())">
                            <xsl:value-of select="."/>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:value-of
                                    select="concat(//eadid/text(), concat(' - ', .))"/>
                        </xsl:otherwise>
                    </xsl:choose>
                </unitid>
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
                    test="@type='file reference' or @type='code-de-communication' or @type='cote de communication' or @type='Aktenzeichen' or @type='aktenzeichen' or @type='Znak teczki'">
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
            <xsl:when test="@type='blank'"/>
            <xsl:otherwise>
                <xsl:call-template name="excludeElement"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

</xsl:stylesheet>