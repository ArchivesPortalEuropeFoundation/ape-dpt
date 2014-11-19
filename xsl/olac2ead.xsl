<xsl:stylesheet version="2.0" xmlns="urn:isbn:1-931666-22-9"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format"
                xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                xmlns:none="none"
                xmlns:ape="http://www.archivesportaleurope.net/functions"
                xmlns:xlink="http://www.w3.org/1999/xlink"
		        xmlns:olac="http://www.language-archives.org/OLAC/1.1/"
                xmlns:dc="http://purl.org/dc/elements/1.1/"
		        xmlns:dcterms="http://purl.org/dc/terms/"
                exclude-result-prefixes="xsl fo xs none ape olac dc dcterms"
                xsi:schemaLocation="urn:isbn:1-931666-22-9 http://www.loc.gov/ead/ead.xsd">

    <xsl:output indent="yes" method="xml" />

    <xsl:template match="*" name="excludeElement"></xsl:template>

    <xsl:template match="/">
        <ead xmlns="urn:isbn:1-931666-22-9"
             xmlns:xlink="http://www.w3.org/1999/xlink"
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="urn:isbn:1-931666-22-9 http://www.archivesportaleurope.net/Portal/profiles/apeEAD.xsd http://www.w3.org/1999/xlink http://www.loc.gov/standards/xlink/xlink.xsd" audience="external">
            <eadheader>
                <eadid />
                <filedesc>
                    <titlestmt>
                        <titleproper />
                    </titlestmt>
                </filedesc>
            </eadheader>
            <archdesc level="fonds">
                <did>
                    <!--unitid/>
                    <unittitle/-->
                </did>
                <!--<xsl:apply-templates select="descendant::*[5]/*[name()='metadata']//*[local-name()='subject' or local-name()='description' or local-name()='type' or local-name()='rights' or local-name()='spatial' or local-name()='abstract']"/>-->
                <xsl:variable name="firstId">
                    <xsl:value-of select="descendant::*[5][name()='metadata']//*[local-name()='identifier']" />
                </xsl:variable>
                <dsc type="othertype">
                    <xsl:for-each select="//*[name()='metadata']">
                        <xsl:if test="not(preceding-sibling::*[name()='metadata']//*[local-name()='identifier']/text() = .//*[local-name()='identifier']/text())">
                            <xsl:call-template name="record_c">
                                <xsl:with-param name="firstId" select="$firstId"/>
                            </xsl:call-template>
                        </xsl:if>
                    </xsl:for-each>
                </dsc>
            </archdesc>
        </ead>
    </xsl:template>


    <xsl:template name="record_c">
        <xsl:param name="firstId"/>
        <xsl:choose>
            <xsl:when test="not(.//*[local-name()='identifier']/text() = $firstId)">
                <c>
                    <xsl:variable name="level">
                        <xsl:value-of select="./*[local-name()='olac']/*[local-name()='type']"/>
                    </xsl:variable>
                    <!--<xsl:attribute name="level">-->
                        <!--<xsl:value-of select="'fonds'"/>-->
                    <!--</xsl:attribute>-->
                    <did>
                        <xsl:apply-templates select=".//*[not(local-name()='description' or local-name()='rights')]"/>
                    </did>
                    <xsl:apply-templates select=".//*[local-name()='description']"/>
                    <xsl:variable name="actual_id">
                        <xsl:value-of select=".//*[local-name()='identifier']/text()"/>
                    </xsl:variable>
                    <xsl:for-each select="following-sibling::*[name()='metadata']">
                        <xsl:if test=".//*[name()='identifier']/text() = $actual_id">
                            <xsl:call-template name="record_c">
                                <xsl:with-param name="firstId" select="''"/>
                            </xsl:call-template>
                        </xsl:if>
                    </xsl:for-each>
                </c>
            </xsl:when>
            <xsl:otherwise>
                <xsl:variable name="actual_id">
                    <xsl:value-of select=".//*[name()='identifier']/text()"/>
                </xsl:variable>
                <xsl:for-each select="following-sibling::*[name()='metadata']">
                    <xsl:if test=".//*[name()='part_of_reference']/text() = $actual_id">
                        <xsl:call-template name="record_c">
                            <xsl:with-param name="firstId" select="''"/>
                        </xsl:call-template>
                    </xsl:if>
                </xsl:for-each>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template match="text()" priority="2">
        <xsl:value-of select="normalize-space(.)"/>
    </xsl:template>

    <xsl:template match="*[local-name()='identifier']">
        <!--The link to the record-->
        <unitid>
            <xsl:value-of select="ape:oai2EadNormalization(.)"/>
        </unitid>
    </xsl:template>

    <xsl:template match="*[local-name()='title']">
        <!--did/unititle-->
        <unittitle>
            <xsl:apply-templates />
        </unittitle>
    </xsl:template>

    <xsl:template match="*[local-name()='date']">
        <!--did/unitdate or if empty did/unittitle/unitdate-->
        <unitdate>
            <xsl:apply-templates />
        </unitdate>
    </xsl:template>

    <xsl:template match="*[local-name()='creator']">
        <!--did/origination-->
	<origination>
	    <xsl:apply-templates />
	</origination>
    </xsl:template>

    <xsl:template match="*[local-name()='language']">
        <!--did/langmaterial/language or if empty did/langmaterial-->
	<langmaterial>
	    <xsl:apply-templates />
	</langmaterial>
    </xsl:template>

    <xsl:template match="*[local-name()='format']">
        <!--did/physdesc-->
	<physdesc>
	    <xsl:apply-templates />
	</physdesc>
    </xsl:template>

    <xsl:template match="*[local-name()='publisher']">
        <!--did/repository or if empty name of the application??-->
        <repository>
            <xsl:apply-templates />
        </repository>
    </xsl:template>

    <xsl:template match="*[local-name()='subject']">
        <!--controlaccess/* (to check in examples)-->
    </xsl:template>

    <xsl:template match="*[local-name()='description']">
        <!--scopecontent-->
        <scopecontent>
            <p><xsl:apply-templates /></p>
        </scopecontent>
    </xsl:template>

    <xsl:template match="*[local-name()='type']">
        <!--controlaccess/genreform AND level of archdesc or c-->
    </xsl:template>

    <xsl:template match="*[local-name()='rights']">
        <!--userestrict/address/addressline or if empty userestrict-->
	    <userestrict>
	        <xsl:apply-templates />
	    </userestrict>
    </xsl:template>

    <xsl:template match="*[local-name()='source']">
        <!--did/repository + ',' + unitid-->
        <!--<repository>-->
            <!--<xsl:apply-templates />-->
        <!--</repository>-->
    </xsl:template>

    <xsl:template match="*[local-name()='abstract']">
        <!--did/abstract-->
	<scopecontent encodinganalog="abstract">
	    <xsl:apply-templates />
	</scopecontent>
    </xsl:template>

    <xsl:template match="*[local-name()='spatial']">
        <!--controlaccess/geogname-->
    </xsl:template>

    <xsl:template match="*[local-name()='hasPart']">
        <!--URL of c child -->
    </xsl:template>

    <xsl:template match="*[local-name()='isPartOf']">
        <!--URL of c parent or archdesc-->
    </xsl:template>

</xsl:stylesheet>
