<?xml version="1.0" encoding="UTF-8"?>
<!--
	List of FAs into a HG
-->
<xsl:stylesheet version="2.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xlink="http://www.w3.org/1999/xlink"
                xmlns:ape="http://www.archivesportaleurope.net/functions"
                exclude-result-prefixes="#all">

 	<xsl:param name="title" select="''"/>
 	<xsl:param name="prefix" select="''"/>
    <xsl:output indent="yes" method="xml" omit-xml-declaration="yes"/>

    <xsl:template match="/">
    	<xsl:variable name="unitid">
			<xsl:choose>
				<xsl:when test="/*:ead/*:archdesc/*:did/*:unitid[@type='call number']">
					<xsl:value-of select="/*:ead/*:archdesc/*:did/*:unitid[@type='call number']/text()"/>
               </xsl:when>
				<xsl:when test="/*:ead/*:archdesc/*:did/*:unitid">
					<xsl:value-of select="/*:ead/*:archdesc/*:did/*:unitid/text()"/>
                </xsl:when>                            	
            </xsl:choose>
    	</xsl:variable>
    	<xsl:variable name="unittitle">
			<xsl:choose>
				<xsl:when test="$title = 'titleproper'">
					<xsl:value-of select="/*:ead/*:eadheader/*:filedesc/*:titlestmt/*:titleproper/text()"/>
               </xsl:when>
				<xsl:otherwise>
					<xsl:if test="/*:ead/*:archdesc/*:did/*:unittitle[not(@type='short')]">
						<xsl:value-of select="//*:archdesc/*:did/*:unittitle[not(@type='short')]/text()"/>
					</xsl:if>
                </xsl:otherwise>                            	
            </xsl:choose>
    	</xsl:variable>
        <xsl:choose>
            <xsl:when test="/*:ead/*:eadheader/*:eadid/text()">
                      <c level="item" xmlns="urn:isbn:1-931666-22-9" xmlns:xlink="http://www.w3.org/1999/xlink">
                            <did>
                           		<xsl:if test="$unitid">
									<unitid encodinganalog="3.1.1" type="call number"><xsl:value-of select="$unitid"/></unitid>
                            	</xsl:if>
                            	<unittitle encodinganalog="3.1.2">
                            	<xsl:if test="$prefix = 'unitid'"><xsl:value-of select="$unitid"/><xsl:text> </xsl:text></xsl:if>
								<xsl:value-of select="$unittitle"/>
                                </unittitle>
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
            <xsl:otherwise><xsl:message>NO_EADID_IN_FILE</xsl:message></xsl:otherwise>
        </xsl:choose>
    </xsl:template>
</xsl:stylesheet>
