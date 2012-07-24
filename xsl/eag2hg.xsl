<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns="urn:isbn:1-931666-22-9"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format"
                xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                xmlns:xlink="http://www.w3.org/1999/xlink"
				xmlns:ape="http://www.archivesportaleurope.eu/functions"
                xsi:schemaLocation="urn:isbn:1-931666-22-9 http://www.loc.gov/ead/ead.xsd"
                xpath-default-namespace="urn:isbn:1-931666-22-9"
                exclude-result-prefixes="xsl fo xs ape">

                <xsl:param name="mainagencycode" select="''"/>
                <xsl:param name="countrycode" select="''"/>

				<xsl:output indent="yes" method="xml" />

				<xsl:template match="/">
					<ead xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns="urn:isbn:1-931666-22-9" xsi:schemaLocation="urn:isbn:1-931666-22-9 http://www.archivesportaleurope.eu/profiles/APEnet_EAD.xsd http://www.w3.org/1999/xlink http://www.loc.gov/standards/xlink/xlink.xsd" audience="external">
						<eadheader countryencoding="iso3166-1" dateencoding="iso8601" langencoding="iso639-2b" repositoryencoding="iso15511" scriptencoding="iso15924" relatedencoding="MARC21">
							<eadid>
                                <xsl:attribute name="countrycode" select="$countrycode"/>
                                <xsl:attribute name="mainagencycode" select="$mainagencycode"/>
                                <xsl:attribute name="identifier">
                                    <xsl:value-of select="$mainagencycode"/><xsl:text>_HG_</xsl:text><xsl:value-of select="//*:eag/*:eagid"/>
                                </xsl:attribute>
							</eadid>
							<filedesc><titlestmt><titleproper/></titlestmt></filedesc>
						</eadheader>
						<archdesc level="fonds" encodinganalog="3.1.4">
							<did>
								<unittitle>
                                    <xsl:value-of select="/*:eag/*:archguide/*:identity/*:autform" /> <xsl:value-of select="/*:eag/*:archguide/*:identity/*:repositorid/@countrycode" />-<xsl:value-of select="/*:eag/*:archguide/*:identity/*:repositorid/@repositorycode" />
                                </unittitle>
							</did>
							<dsc type="othertype">
								<xsl:apply-templates select="//*:descunit"/>
							</dsc>
						</archdesc>
					</ead>
				</xsl:template>

				<xsl:template match="node()">
					<xsl:apply-templates select="node()"/>
				</xsl:template>

				<xsl:template match="*:descunit">
					<c level="item">
						<did>
							<xsl:apply-templates />
						</did>
                        <otherfindaid>
                            <p>
                                <extref xmlns:xlink="http://www.w3.org/1999/xlink">
                                    <xsl:attribute name="xlink:href">
                                        <xsl:value-of select="ape:normalizeDate(./child::*[name()='unitid']/text(), 'pl_unitid')"/>
                                    </xsl:attribute>
                                </extref>
                            </p>
                        </otherfindaid>
					</c>
				</xsl:template>
				<xsl:template match="*:unitid">
					<unitid>
						<xsl:value-of select="ape:normalizeDate(text(), 'pl_unitid')"/>
					</unitid>
				</xsl:template>
				<xsl:template match="*:unittitle">
					<unittitle>
						<xsl:value-of select="text()" />
					</unittitle>
				</xsl:template>
				<xsl:template match="*:date">
					<unitdate calendar="gregorian" era="ce">
						<xsl:if test="@normal">
							<xsl:variable name="normal">
			                    <xsl:value-of select="ape:normalizeDate(normalize-space(@normal))"/>
			                </xsl:variable>
							<xsl:if test="normalize-space($normal)">
		                        <xsl:attribute name="normal">
		                            <xsl:value-of select="$normal"/>
		                        </xsl:attribute>
		                    </xsl:if>
						</xsl:if>
						<xsl:value-of select="text()" />
					</unitdate>
				</xsl:template>
				<xsl:template match="*:extent">
					<physdesc encodinganalog="3.1.5">
						<xsl:apply-templates />
					</physdesc>
				</xsl:template>
				<xsl:template match="*:num">
					<extent>
						<xsl:if test="@unit">
							<xsl:attribute name="unit" select="@unit" />
						</xsl:if>
						<xsl:value-of select="text()" />
					</extent>
				</xsl:template>

				<xsl:template match="comment()" priority="3" />

</xsl:stylesheet>
