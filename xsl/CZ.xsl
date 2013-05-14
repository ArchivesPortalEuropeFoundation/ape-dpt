<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns="urn:isbn:1-931666-22-9"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format"
                xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                xmlns:xlink="http://www.w3.org/1999/xlink"
                xsi:schemaLocation="urn:isbn:1-931666-22-9 http://www.loc.gov/ead/ead.xsd"
                xpath-default-namespace="urn:isbn:1-931666-22-9"
                exclude-result-prefixes="xsl fo xs">

    <xsl:output indent="yes" method="xml" />

    <xsl:template match="/">
        <ead>
            <eadheader>
                <eadid countrycode="CZ" mainagencycode="CZ-TEST" identifier="CZ-TEST-TEST">TEST</eadid>
                <filedesc>
                    <titlestmt>
                        <titleproper>TEST</titleproper>
                    </titlestmt>
                </filedesc>
            </eadheader>
            <archdesc level="fonds">
                <did>
                    <unitid>TEST</unitid>
                </did>
                <dsc>
                    <xsl:apply-templates select="/*:dokumentArchPomucky/*:pomucka/*:inventSeznam/*:castIS"/>
                </dsc>
            </archdesc>
        </ead>
    </xsl:template>

    <xsl:template match="*:castIS">
        <c>
            <xsl:choose>
                <xsl:when test="@aUrovenVn = '1'">
                    <xsl:attribute name="level" select="'fonds'"/>
                </xsl:when>
                <xsl:when test="@aUrovenVn = '2'">
                    <xsl:attribute name="level" select="'series'"/>
                </xsl:when>
                <xsl:when test="@aUrovenVn = '3'">
                    <xsl:attribute name="level" select="'subseries'"/>
                </xsl:when>
                <xsl:when test="@aUrovenVn = '4'">
                    <xsl:attribute name="level" select="'file'"/>
                </xsl:when>
            </xsl:choose>
            <xsl:apply-templates />
        </c>
    </xsl:template>

    <xsl:template match="*:nadpisIS">
        <did>
            <xsl:if test="../@aID">
                <unitid>
                    <xsl:value-of select="../@aID"/>
                </unitid>
            </xsl:if>
            <xsl:if test="*:obsah">
                <unittitle>
                    <xsl:value-of select="*:obsah"/>
                </unittitle>
            </xsl:if>
        </did>
    </xsl:template>

    <xsl:template match="*:radekIS">
         <did>
            <xsl:if test="../@aID">
                <unitid>
                    <xsl:value-of select="../@aID"/>
                </unitid>
            </xsl:if>
             <xsl:if test="*:obsah">
                <unittitle>
                    <xsl:value-of select="*:obsah"/>
                </unittitle>
            </xsl:if>
             <xsl:if test="*:datace">
                <unitdate>
                    <xsl:value-of select="*:datace/*:datum"/>
                </unitdate>
            </xsl:if>
        </did>
    </xsl:template>

    <xsl:template match="text()|@*" priority="2">
        <xsl:copy>
            <xsl:apply-templates select="node()|@*"/>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="comment()" priority="3" />

</xsl:stylesheet>
