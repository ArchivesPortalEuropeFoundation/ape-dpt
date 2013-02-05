<?xml version="1.0" encoding="UTF-8"?>
<!--
	EAG: Changing the EAG namespace to comply with the schema
-->
<xsl:stylesheet version="2.0" xmlns="http://www.archivesportaleurope.net/Portal/profiles/eag_2012/"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format"
                xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                xmlns:xlink="http://www.w3.org/1999/xlink"
                xpath-default-namespace="http://www.archivesportaleurope.net/Portal/profiles/eag_2012/"
                exclude-result-prefixes="xsl fo xs">

				<xsl:output indent="yes" method="xml" />

				<xsl:template match="node()">
					<xsl:element name="{local-name()}" namespace="http://www.archivesportaleurope.net/Portal/profiles/eag_2012/">
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
