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
				
				<xsl:template match="node()">
                    <xsl:choose>
                        <xsl:when test="not(local-name()='c01' or local-name()='c02' or local-name()='c03' or local-name()='c04' or local-name()='c05' or local-name()='c06' or local-name()='c07' or local-name()='c08' or local-name()='c09' or local-name()='c10' or local-name()='c11' or local-name()='c12')">
                            <xsl:element name="{local-name()}" namespace="urn:isbn:1-931666-22-9">
                                <xsl:apply-templates select="node()|@*"/>
                            </xsl:element>                            
                        </xsl:when>
                        <xsl:otherwise>
                            <c>
                                <xsl:apply-templates select="node()|@*"/>
                            </c>
                        </xsl:otherwise>
                    </xsl:choose>
				</xsl:template>

				<xsl:template match="text()|@*" priority="2">
					<xsl:copy>
						<xsl:apply-templates select="node()|@*"/>
					</xsl:copy>
				</xsl:template>
				
				<xsl:template match="comment()" priority="3" />

</xsl:stylesheet>
