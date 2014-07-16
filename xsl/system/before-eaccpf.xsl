<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns="urn:isbn:1-931666-33-4"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format"
                xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                xsi:schemaLocation="urn:isbn:1-931666-33-4 http://www.archivesportaleurope.net/Portal/profiles/apeEAC-CPF.xsd"
                xpath-default-namespace="urn:isbn:1-931666-33-4"
                exclude-result-prefixes="xsl fo xs">

    <xsl:output indent="yes" method="xml" />

    <xsl:template match="processing-instruction()" priority="1"/>

    <xsl:template match="node()">
        <xsl:element name="{local-name()}" namespace="urn:isbn:1-931666-33-4">
                    <xsl:apply-templates select="node()|@*"/>
        </xsl:element>
    </xsl:template>

    <xsl:template match="text()|@*" priority="2">
        <xsl:copy>
            <xsl:apply-templates select="node()|@*"/>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="comment()" priority="3" />

</xsl:stylesheet>
