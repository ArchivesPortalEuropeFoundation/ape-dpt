<?xml version="1.0" encoding="UTF-8"?>
<!--
	List of FAs into a HG
-->
<xsl:stylesheet version="2.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xlink="http://www.w3.org/1999/xlink"
                xmlns:ape="http://www.archivesportaleurope.net/functions"
                exclude-result-prefixes="#all">

    <xsl:param name="addXMLNS" select="'false'"/>

    <xsl:output indent="yes" method="xml" omit-xml-declaration="yes"/>

    <xsl:template match="/">
        <xsl:choose>
            <xsl:when test="/*:ead/*:eadheader/*:eadid/text()">
                <xsl:choose>
                    <xsl:when test="$addXMLNS = 'true'">
                        <c level="item" xmlns="urn:isbn:1-931666-22-9" xmlns:xlink="http://www.w3.org/1999/xlink">
                            <did>
                                <xsl:if test="/*:ead/*:archdesc/*:did/*:unitid[@type='call number']">
                                    <unitid encodinganalog="3.1.1"><xsl:value-of select="//*:archdesc/*:did/*:unitid[@type='call number']/text()"/></unitid>
                                </xsl:if>
                                <xsl:if test="/*:ead/*:archdesc/*:did/*:unittitle[not(@type='short')]">
                                    <unittitle encodinganalog="3.1.2"><xsl:value-of select="//*:archdesc/*:did/*:unittitle[not(@type='short')]/text()"/></unittitle>
                                </xsl:if>
                                <xsl:for-each select="/*:ead/*:archdesc/*:did/*:unitdate">
                                    <unitdate>
                                        <xsl:attribute name="calendar" select="'gregorian'"/>
                                        <xsl:attribute name="era" select="'ce'"/>
                                        <xsl:attribute name="encodinganalog" select="'3.1.3'"/>
                                        <xsl:if test="normalize-space(@normal)">
                                            <xsl:attribute name="normal">
                                                <xsl:value-of select="ape:normalizeDate(normalize-space(@normal))"/>
                                            </xsl:attribute>
                                        </xsl:if>
                                        <xsl:value-of select="text()"/>
                                    </unitdate>
                                </xsl:for-each>
                            </did>
                            <otherfindaid>
                                <p>
                                    <extref>
                                        <xsl:attribute name="xlink:href" select="/*:ead/*:eadheader/*:eadid/text()"/>
                                    </extref>
                                </p>
                            </otherfindaid>
                        </c>
                    </xsl:when>
                    <xsl:otherwise>
                        <c level="item">
                            <did>
                                <xsl:if test="/*:ead/*:archdesc/*:did/*:unitid[@type='call number']">
                                    <unitid encodinganalog="3.1.1"><xsl:value-of select="/*:ead/*:archdesc/*:did/*:unitid[@type='call number']/text()"/></unitid>
                                </xsl:if>
                                <xsl:if test="/*:ead/*:archdesc/*:did/*:unittitle[not(@type='short')]">
                                    <unittitle encodinganalog="3.1.2"><xsl:value-of select="/*:ead/*:archdesc/*:did/*:unittitle[not(@type='short')]/text()"/></unittitle>
                                </xsl:if>
                                <xsl:for-each select="/*:ead/*:archdesc/*:did/*:unitdate">
                                    <unitdate>
                                        <xsl:attribute name="calendar" select="'gregorian'"/>
                                        <xsl:attribute name="era" select="'ce'"/>
                                        <xsl:attribute name="encodinganalog" select="'3.1.3'"/>
                                        <xsl:if test="normalize-space(@normal)">
                                            <xsl:attribute name="normal">
                                                <xsl:value-of select="ape:normalizeDate(normalize-space(@normal))"/>
                                            </xsl:attribute>
                                        </xsl:if>
                                        <xsl:value-of select="text()"/>
                                    </unitdate>
                                </xsl:for-each>
                            </did>
                            <otherfindaid>
                                <p>
                                    <extref>
                                        <xsl:attribute name="xlink:href" select="/*:ead/*:eadheader/*:eadid/text()"/>
                                    </extref>
                                </p>
                            </otherfindaid>
                        </c>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:when>
            <xsl:otherwise><xsl:message>NO_EADID_IN_FILE</xsl:message></xsl:otherwise>
        </xsl:choose>
    </xsl:template>
</xsl:stylesheet>
