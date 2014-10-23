<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns="urn:isbn:1-931666-22-9" xmlns:xlink="http://www.w3.org/1999/xlink"
                xpath-default-namespace="urn:isbn:1-931666-22-9" version="2.0">

    <xsl:strip-space elements="*" />
    <xsl:output indent="yes" method="xml"/>

    <xsl:template match="node() | @*">
        <xsl:copy>
            <xsl:copy-of select="@*"/>
            <xsl:apply-templates select="node() | @*" />
        </xsl:copy>
    </xsl:template>

    <xsl:template match="text()" priority="1">
        <xsl:value-of select="normalize-space(.)" />
    </xsl:template>

    <xsl:template match="c">
        <xsl:copy>
            <xsl:choose>
                <xsl:when test="count(child::c) eq 0 and count(otherfindaid/p/extref) > 1">
                    <xsl:apply-templates select="node() except otherfindaid | @*" />
                    <xsl:for-each select="otherfindaid/p">
                        <xsl:call-template name="create_subseries_level">
                            <xsl:with-param name="pNode" select="current()"/>
                        </xsl:call-template>
                    </xsl:for-each>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:apply-templates select="node() | @*" />
                </xsl:otherwise>
            </xsl:choose>
        </xsl:copy>
    </xsl:template>

    <xsl:template name="create_subseries_level">
        <xsl:param name="pNode"/>
        <c>
            <did>
                <unittitle encodinganalog="3.1.2">
                    <xsl:value-of select="normalize-space($pNode/text()[1])"/>
                </unittitle>
            </did>
            <xsl:for-each select="extref">
                <xsl:call-template name="create_file_level">
                    <xsl:with-param name="extrefNode" select="current()"/>
                </xsl:call-template>
            </xsl:for-each>
        </c>
    </xsl:template>

    <xsl:template name="create_file_level">
        <xsl:param name="extrefNode"/>
        <c>
            <did>
                <unittitle encodinganalog="3.1.2">
                    <xsl:value-of select="normalize-space($extrefNode/text())"/>
                </unittitle>
            </did>
            <xsl:if test="$extrefNode/@xlink:href">
                <otherfindaid encodinganalog="3.4.5">
                    <p>
                        <extref>
                            <xsl:attribute name="xlink:href" select="$extrefNode/@xlink:href"/>
                        </extref>
                    </p>
                </otherfindaid>
            </xsl:if>
        </c>
    </xsl:template>

</xsl:stylesheet>