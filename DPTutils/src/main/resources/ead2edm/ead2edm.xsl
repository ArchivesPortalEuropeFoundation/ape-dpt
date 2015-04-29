<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:fo="http://www.w3.org/1999/XSL/Format"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:edm="http://www.europeana.eu/schemas/edm/"
    xmlns:enrichment="http://www.europeana.eu/schemas/edm/enrichment"
    xmlns:owl="http://www.w3.org/2002/07/owl#"
    xmlns:wgs84="http://www.w3.org/2003/01/geo/wgs84_pos#"
    xmlns:skos="http://www.w3.org/2004/02/skos/core#"
    xmlns:ore="http://www.openarchives.org/ore/terms/"
    xmlns:oai="http://www.openarchives.org/OAI/2.0"
    xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
    xmlns:dcterms="http://purl.org/dc/terms/"
    xmlns:dc="http://purl.org/dc/elements/1.1/"
    xmlns:europeana="http://www.europeana.eu/schemas/ese/"
    xmlns="http://www.europeana.eu/schemas/edm/"
    xmlns:fn="http://www.w3.org/2005/xpath-functions"
    xmlns:xlink="http://www.w3.org/1999/xlink"
    xpath-default-namespace="urn:isbn:1-931666-22-9"
    exclude-result-prefixes="xlink fo fn">
    <xsl:output method="xml" encoding="UTF-8" indent="yes"/>

    <!-- Params from Ead2Ese -->
    <xsl:param name="europeana_provider"/>
    <xsl:param name="europeana_dataprovider"/>
    <xsl:param name="europeana_rights"/>
    <xsl:param name="dc_rights"/>
    <xsl:param name="europeana_type"/>
    <xsl:param name="useISODates"/>
    <xsl:param name="language"/>
    <xsl:param name="inheritElementsFromFileLevel"/>
    <xsl:param name="inheritOrigination"/>
    <xsl:param name="inheritUnittitle"/>
    <xsl:param name="inheritLanguage"/>
    <xsl:param name="inheritRightsInfo"/>
    <xsl:param name="useExistingDaoRole"/>
    <xsl:param name="useExistingRepository"/>
    <xsl:param name="useExistingLanguage"/>
    <xsl:param name="useExistingRightsInfo"/>
    <xsl:param name="minimalConversion"/>
    <xsl:param name="idSource"/>
    <xsl:param name="landingPage"/>
    <!-- Params from Ese2Edm -->
    <xsl:param name="edm_identifier"/>
    <xsl:param name="host"/>
    <xsl:param name="repository_code"/>
    <xsl:param name="xml_type_name"/>
    <!-- Variables -->
    <xsl:variable name="id_base"
        select="concat('http://', $host, '/ead-display/-/ead/pl/aicode/' , $repository_code, '/type/', $xml_type_name, '/id/')"/>
    <!-- Key for detection of unitid duplicates -->
    <xsl:key name="unitids" match="unitid" use="text()"></xsl:key>

    <xsl:template match="/">
        <rdf:RDF
            xsi:schemaLocation="http://www.europeana.eu/schemas/edm/ http://www.europeana.eu/schemas/edm/EDM.xsd">
            <xsl:apply-templates/>
        </rdf:RDF>
    </xsl:template>

    <xsl:template match="eadheader">
        <xsl:apply-templates select="eadid"/>
    </xsl:template>

    <xsl:template match="eadid">
        <ore:Aggregation>
            <xsl:attribute name="rdf:about" select="concat('aggregation_', .)"/>
            <edm:aggregatedCHO>
                <xsl:attribute name="rdf:resource" select="concat('providedCHO_', .)"/>
            </edm:aggregatedCHO>
            <xsl:choose>
                <xsl:when test="$useExistingRepository='true'">
                    <xsl:choose>
                        <xsl:when test="/ead/archdesc/did/repository[1]">
                            <xsl:apply-templates select="/ead/archdesc/did/repository[1]"/>
                        </xsl:when>
                        <xsl:otherwise>
                            <edm:dataProvider>
                                <xsl:value-of select="$europeana_dataprovider"/>
                            </edm:dataProvider>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:when>
                <xsl:otherwise>
                    <edm:dataProvider>
                        <xsl:value-of select="$europeana_dataprovider"/>
                    </edm:dataProvider>
                </xsl:otherwise>
            </xsl:choose>
            <edm:isShownAt>
                <xsl:choose>
                    <xsl:when test="@url">
                        <xsl:attribute name="rdf:resource" select="@url"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:choose>
                            <xsl:when test="$landingPage = 'ape'">
                                <xsl:attribute name="rdf:resource" select="concat($id_base, normalize-space(.))"/>
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:attribute name="rdf:resource" select="normalize-space($landingPage)"/>
                            </xsl:otherwise>
                        </xsl:choose>
                    </xsl:otherwise>
                </xsl:choose>
            </edm:isShownAt>
            <edm:isShownBy>
                <xsl:attribute name="rdf:resource"
                    select="concat('http://', $host, '/Portal-theme/images/ape/icons/dao_types/europeana/text.png')"
                />
            </edm:isShownBy>
            <edm:provider>
                <xsl:value-of select="$europeana_provider"/>
            </edm:provider>
            <edm:rights>
                <xsl:attribute name="rdf:resource"
                    select="'http://creativecommons.org/publicdomain/zero/1.0/'"/>
            </edm:rights>
        </ore:Aggregation>
        <edm:ProvidedCHO>
            <xsl:attribute name="rdf:about" select="concat('providedCHO_', .)"/>
            <xsl:if test="/ead/archdesc/did/origination">
                <xsl:for-each select="/ead/archdesc/did/origination">
                    <dc:creator>
                        <xsl:value-of select="normalize-space(.)"/>
                    </dc:creator>
                </xsl:for-each>
            </xsl:if>
            <xsl:if test="/ead/archdesc/scopecontent[@encodinganalog=&quot;summary&quot;]">
                <xsl:apply-templates select="/ead/archdesc/scopecontent[@encodinganalog=&quot;summary&quot;]" />
            </xsl:if>
            <xsl:if test="/ead/archdesc/did/unitid">
                <dc:identifier>
                    <xsl:value-of select="/ead/archdesc/did/unitid"/>
                </dc:identifier>
            </xsl:if>
            <dc:language>
                <xsl:value-of select="$language"/>
            </dc:language>
            <dc:subject>
                <xsl:apply-templates select="/ead/archdesc/@level"/>
            </dc:subject>
            <xsl:if test="/ead/archdesc/did/unittitle">
                <xsl:apply-templates select="/ead/archdesc/did/unittitle"/>
            </xsl:if>
            <xsl:if test="/ead/archdesc/relatedmaterial">
                <xsl:apply-templates select="/ead/archdesc/relatedmaterial"/>
            </xsl:if>
            <xsl:if test="/ead/archdesc/custodhist">
                <xsl:call-template name="custodhist">
                    <xsl:with-param name="custodhists" select="/ead/archdesc/custodhist" />
                </xsl:call-template>
            </xsl:if>
            <dc:type>
                <xsl:choose>
                    <xsl:when test="/ead/archdesc/did/physdesc/genreform">
                        <xsl:value-of select="/ead/archdesc/did/physdesc/genreform"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:value-of select="'Archival material'"/>
                    </xsl:otherwise>
                </xsl:choose>
            </dc:type>
            <xsl:if test="/ead/archdesc/dsc/c">
                <xsl:for-each select="/ead/archdesc/dsc/c">
                    <xsl:variable name="currentCPosition">
                        <xsl:call-template name="number">
                            <xsl:with-param name="node" select="."></xsl:with-param>
                        </xsl:call-template>
                    </xsl:variable>
                    <xsl:variable name="isFirstUnitid">
                        <xsl:call-template name="detectFirstUnitid">
                            <xsl:with-param name="positionInDocument" select="$currentCPosition"/>
                            <xsl:with-param name="currentCNode" select="."/>
                        </xsl:call-template>
                    </xsl:variable>
                    <xsl:if test="descendant::dao">
                        <xsl:choose>
                            <xsl:when test="$idSource = 'unitid' and did/unitid[@type='call number'] and $isFirstUnitid = 'true'">
                                <dcterms:hasPart>
                                    <xsl:attribute name="rdf:resource" select="concat('providedCHO_', normalize-space(did/unitid[@type='call number']))"/>
                                </dcterms:hasPart>
                            </xsl:when>
                            <xsl:when test="$idSource = 'cid' and @id">
                                <dcterms:hasPart>
                                    <xsl:attribute name="rdf:resource" select="concat('providedCHO_', normalize-space(@id))"/>
                                </dcterms:hasPart>
                            </xsl:when>
                            <xsl:otherwise>
                                <dcterms:hasPart>
                                    <xsl:attribute name="rdf:resource">
                                        <xsl:call-template name="number">
                                            <xsl:with-param name="prefix" select="'providedCHO_position_'"/>
                                            <xsl:with-param name="node" select="."/>
                                        </xsl:call-template>
                                    </xsl:attribute>
                                </dcterms:hasPart>
                            </xsl:otherwise>
                        </xsl:choose>
                    </xsl:if>
                </xsl:for-each>
            </xsl:if>
            <xsl:if test="/ead/archdesc/did/unitdate">
                <dcterms:temporal>
                    <xsl:value-of select="/ead/archdesc/did/unitdate"/>
                </dcterms:temporal>
            </xsl:if>
            <edm:type>
                <xsl:value-of select="'TEXT'"/>
            </edm:type>
        </edm:ProvidedCHO>
        <edm:WebResource>
            <xsl:attribute name="rdf:about">
                <xsl:choose>
                    <xsl:when test="@url">
                        <xsl:value-of select="@url"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:choose>
                            <xsl:when test="$landingPage = 'ape'">
                                <xsl:attribute name="rdf:resource" select="concat($id_base, normalize-space(.))"/>
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:attribute name="rdf:resource" select="normalize-space($landingPage)"/>
                            </xsl:otherwise>
                        </xsl:choose>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:attribute>
            <dc:description>
                <xsl:choose>
                    <xsl:when test="/ead/archdesc/did/unittitle">
                        <xsl:apply-templates select="/ead/archdesc/did/unittitle" mode="dcDescription"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:value-of select="'Archival material'"/>
                    </xsl:otherwise>
                </xsl:choose>
            </dc:description>
            <edm:rights rdf:resource="http://creativecommons.org/publicdomain/zero/1.0/"/>
        </edm:WebResource>
    </xsl:template>

    <xsl:template match="filedesc|profiledesc|revisiondesc"/>

    <xsl:template match="ead/archdesc">
        <xsl:apply-templates select="dsc/c">
            <xsl:with-param name="inheritedOriginations">
                <xsl:if test="./did/origination">
                    <xsl:call-template name="creator">
                        <xsl:with-param name="originations" select="./did/origination"/>
                    </xsl:call-template>
                </xsl:if>
            </xsl:with-param>
            <xsl:with-param name="inheritedLanguages">
                <xsl:if test="./did/langmaterial">
                    <xsl:call-template name="language">
                        <xsl:with-param name="langmaterials" select="./did/langmaterial"/>
                    </xsl:call-template>
                </xsl:if>
            </xsl:with-param>
            <xsl:with-param name="inheritedCustodhists">
                <xsl:if test="./custodhist">
                    <xsl:call-template name="custodhist">
                        <xsl:with-param name="custodhists" select="./custodhist"/>
                    </xsl:call-template>
                </xsl:if>
            </xsl:with-param>
            <xsl:with-param name="inheritedAltformavails">
                <xsl:if test="./altformavail">
                    <xsl:call-template name="altformavail">
                        <xsl:with-param name="altformavails" select="./altformavail"/>
                    </xsl:call-template>
                </xsl:if>
            </xsl:with-param>
            <xsl:with-param name="inheritedControlaccesses">
                <xsl:if test="./controlaccess">
                    <xsl:call-template name="controlaccess">
                        <xsl:with-param name="controlaccesses" select="./controlaccess"/>
                    </xsl:call-template>
                </xsl:if>
            </xsl:with-param>
            <xsl:with-param name="inheritedRepository">
                <xsl:if test="./did/repository">
                    <xsl:call-template name="repository">
                        <xsl:with-param name="repository" select="./did/repository"/>
                    </xsl:call-template>
                </xsl:if>
            </xsl:with-param>
            <xsl:with-param name="inheritedRightsInfo">
                <xsl:if test="./userestrict">
                    <xsl:call-template name="createRights">
                        <xsl:with-param name="rights" select="./userestrict"/>
                    </xsl:call-template>
                </xsl:if>
            </xsl:with-param>
        </xsl:apply-templates>
    </xsl:template>

    <xsl:template match="c">
        <xsl:param name="inheritedOriginations"/>
        <xsl:param name="inheritedLanguages"/>
        <xsl:param name="inheritedCustodhists"/>
        <xsl:param name="inheritedAltformavails"/>
        <xsl:param name="inheritedControlaccesses"/>
        <xsl:param name="inheritedRepository"/>
        <xsl:param name="inheritedRightsInfo"/>
        <xsl:param name="positionChain"/>

        <xsl:variable name="updatedInheritedOriginations">
            <xsl:choose>
                <xsl:when test="$inheritOrigination = 'true' and ./did/origination">
                    <xsl:call-template name="creator">
                        <xsl:with-param name="originations" select="./did/origination"/>
                    </xsl:call-template>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:copy-of select="$inheritedOriginations"/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <xsl:variable name="updatedInheritedLanguages">
            <xsl:choose>
                <xsl:when test="$inheritLanguage = 'true' and ./did/langmaterial">
                    <xsl:call-template name="language">
                        <xsl:with-param name="langmaterials" select="./did/langmaterial"/>
                    </xsl:call-template>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:copy-of select="$inheritedLanguages"/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <xsl:variable name="updatedInheritedCustodhists">
            <xsl:choose>
                <xsl:when test="$inheritElementsFromFileLevel = 'true' and ./custodhist">
                    <xsl:call-template name="custodhist">
                        <xsl:with-param name="custodhists" select="./custodhist"/>
                    </xsl:call-template>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:copy-of select="$inheritedCustodhists"/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <xsl:variable name="updatedInheritedAltformavails">
            <xsl:choose>
                <xsl:when test="$inheritElementsFromFileLevel = 'true' and ./altformavail">
                    <xsl:call-template name="altformavail">
                        <xsl:with-param name="altformavails" select="./altformavail"/>
                    </xsl:call-template>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:copy-of select="$inheritedAltformavails"/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <xsl:variable name="updatedInheritedControlaccesses">
            <xsl:choose>
                <xsl:when test="$inheritElementsFromFileLevel = 'true' and ./controlaccess">
                    <xsl:call-template name="controlaccess">
                        <xsl:with-param name="controlaccesses" select="./controlaccess"/>
                    </xsl:call-template>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:copy-of select="$inheritedControlaccesses"/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <xsl:variable name="updatedInheritedRepository">
            <xsl:choose>
                <xsl:when test="./did/repository">
                    <xsl:call-template name="repository">
                        <xsl:with-param name="repository" select="./did/repository"/>
                    </xsl:call-template>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:copy-of select="$inheritedRepository"/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <xsl:variable name="updatedInheritedRightsInfo">
            <xsl:choose>
                <xsl:when test="$inheritRightsInfo = 'true' and ./userestrict[@type='dao']">
                    <xsl:call-template name="createRights">
                        <xsl:with-param name="rights" select="./userestrict[@type='dao']"/>
                    </xsl:call-template>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:copy-of select="$inheritedRightsInfo"/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <!--<xsl:variable name="parentcnode" select="parent::node()"/>-->
        <!--<xsl:variable name="parentdidnode" select="$parentcnode/did"/>-->
        <xsl:variable name="positionInDocument">
            <xsl:call-template name="number">
                <xsl:with-param name="node" select="node()"/>
            </xsl:call-template>
        </xsl:variable>

        <!-- CREATE LEVEL INFORMATION IF C OR DESCENDANT OF C HAS DAO -->
        <xsl:if test="descendant::did/dao">
            <xsl:call-template name="addRecord">
                <xsl:with-param name="currentnode" select="."/>
                <xsl:with-param name="inheritedOriginations" select="$updatedInheritedOriginations"/>
                <xsl:with-param name="inheritedLanguages" select="$updatedInheritedLanguages"/>
                <xsl:with-param name="inheritedCustodhists" select="$updatedInheritedCustodhists"/>
                <xsl:with-param name="inheritedAltformavails" select="$updatedInheritedAltformavails"/>
                <xsl:with-param name="inheritedControlaccesses" select="$updatedInheritedControlaccesses"/>
                <xsl:with-param name="inheritedRepository" select="$updatedInheritedRepository"/>
                <xsl:with-param name="inheritedRightsInfo" select="$updatedInheritedRightsInfo"/>
                <xsl:with-param name="positionChain" select="$positionChain"/>
                <xsl:with-param name="mainIdentifier">
                    <xsl:choose>
                        <xsl:when test="$positionChain">
                            <xsl:value-of select="concat($positionChain, '-', $positionInDocument)"/>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:value-of select="$positionInDocument"/>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:with-param>
            </xsl:call-template>
        </xsl:if>

        <!-- GO FURTHER DOWN THE LEVEL HIERARCHY -->
        <xsl:if test="count(child::c) > 0">
            <xsl:apply-templates select="c">
                <xsl:with-param name="inheritedOriginations" select="$updatedInheritedOriginations"/>
                <xsl:with-param name="inheritedLanguages" select="$updatedInheritedLanguages"/>
                <xsl:with-param name="inheritedCustodhists" select="$updatedInheritedCustodhists"/>
                <xsl:with-param name="inheritedAltformavails" select="$updatedInheritedAltformavails"/>
                <xsl:with-param name="inheritedControlaccesses" select="$updatedInheritedControlaccesses"/>
                <xsl:with-param name="inheritedRepository" select="$updatedInheritedRepository"/>
                <xsl:with-param name="inheritedRightsInfo" select="$updatedInheritedRightsInfo"/>
                <xsl:with-param name="positionChain">
                    <xsl:choose>
                        <xsl:when test="$positionChain">
                            <xsl:value-of select="concat($positionChain, '-', $positionInDocument)"
                            />
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:value-of select="$positionInDocument"/>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:with-param>
            </xsl:apply-templates>
        </xsl:if>
    </xsl:template>
    
    <xsl:template name="addRecord">
        <xsl:param name="currentnode"/>
        <xsl:param name="inheritedOriginations"/>
        <xsl:param name="inheritedLanguages"/>
        <xsl:param name="inheritedCustodhists"/>
        <xsl:param name="inheritedAltformavails"/>
        <xsl:param name="inheritedControlaccesses"/>
        <xsl:param name="inheritedRepository"/>
        <xsl:param name="inheritedRightsInfo"/>
        <xsl:param name="mainIdentifier"/>
        <xsl:param name="positionChain"/>

        <!-- VARIABLES -->
        <xsl:variable name="linkPosition" select="position()"/>
        <xsl:variable name="parentcnode" select="$currentnode/parent::node()"/>
        <xsl:variable name="inheritFromParent" select="$inheritElementsFromFileLevel=&quot;true&quot;"/>
        <xsl:variable name="parentdidnode" select="$parentcnode/did"/>
        <xsl:variable name="parentofparentcnode" select="$parentcnode/parent::node()"/>
        <xsl:variable name="positionInDocument">
            <xsl:call-template name="number">
                <xsl:with-param name="node" select="$currentnode"/>
            </xsl:call-template>
        </xsl:variable>
        <xsl:variable name="isFirstUnitid">
            <xsl:call-template name="detectFirstUnitid">
                <xsl:with-param name="positionInDocument" select="$positionInDocument"/>
                <xsl:with-param name="currentCNode" select="$currentnode"></xsl:with-param>
            </xsl:call-template>
        </xsl:variable>
        <xsl:variable name="identifier">
            <xsl:choose>
                <xsl:when test="$idSource = 'unitid' and did/unitid[@type='call number'] and $isFirstUnitid = 'true'">
                    <xsl:value-of select="normalize-space(did/unitid[@type='call number'])"/>
                </xsl:when>
                <xsl:when test="$idSource = 'cid' and @id">
                    <xsl:value-of select="normalize-space(@id)"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="concat('position_', $mainIdentifier)"/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <xsl:variable name="hasDao" select="if(did/dao) then true() else false()" />
        
        <!-- for each dao found, create a set of classes -->
        <!--<xsl:for-each select="did/dao[not(@xlink:title=&quot;thumbnail&quot;)]">-->

        <!-- ACTUAL CONVERSION BEGINS HERE -->
        <ore:Aggregation>
            <xsl:attribute name="rdf:about" select="concat('aggregation_',$identifier)"/>
            <edm:aggregatedCHO>
                <xsl:attribute name="rdf:resource" select="concat('providedCHO_',$identifier)"/>
            </edm:aggregatedCHO>
            <xsl:choose>
                <xsl:when test="$useExistingRepository='true'">
                    <xsl:choose>
                        <xsl:when test="$currentnode/did/repository[1]">
                            <xsl:apply-templates select="$currentnode/did/repository[1]"/>
                        </xsl:when>
                        <xsl:when test="$inheritedRepository">
                            <xsl:call-template name="repository">
                                <xsl:with-param name="repository" select="$inheritedRepository"/>
                            </xsl:call-template>
                        </xsl:when>
                        <xsl:otherwise>
                            <edm:dataProvider>
                                <xsl:value-of select="$europeana_dataprovider"/>
                            </edm:dataProvider>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:apply-templates select="/ead/archdesc/did/repository[1]"/>
                </xsl:otherwise>
            </xsl:choose>
            <edm:provider>
                <xsl:value-of select="$europeana_provider"/>
            </edm:provider>
            <xsl:choose>
                <xsl:when test="$hasDao">
                    <xsl:if test="count(did/dao[not(@xlink:title='thumbnail')]) > 1">
                        <xsl:apply-templates select="did/dao[not(@xlink:title='thumbnail')][position() > 1]"
                                             mode="additionalLinks"/>
                    </xsl:if>
                    <xsl:apply-templates select="did/dao[not(@xlink:title='thumbnail')][1]" mode="firstLink"/>
                    <xsl:choose>
                        <xsl:when test="did/dao[@xlink:title='thumbnail']">
                            <xsl:apply-templates select="did/dao[@xlink:title='thumbnail'][1]" mode="thumbnail"/>
                        </xsl:when>
                        <xsl:when test="$useExistingDaoRole='true'">
                            <xsl:choose>
                                <xsl:when test="did/dao[not(@xlink:title='thumbnail')][1]/@xlink:role">
                                    <edm:isShownBy>
                                        <xsl:attribute name="rdf:resource">
                                            <xsl:call-template name="generateThumbnailLink">
                                                <xsl:with-param name="role" select="did/dao[not(@xlink:title='thumbnail')][1]/@xlink:role"/>
                                            </xsl:call-template>
                                        </xsl:attribute>
                                    </edm:isShownBy>
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:if test="fn:string-length($europeana_type) > 0">
                                        <edm:isShownBy>
                                            <xsl:attribute name="rdf:resource">
                                                <xsl:call-template name="generateThumbnailLink">
                                                    <xsl:with-param name="role" select="$europeana_type"/>
                                                </xsl:call-template>
                                            </xsl:attribute>
                                        </edm:isShownBy>
                                    </xsl:if>
                                </xsl:otherwise>
                            </xsl:choose>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:choose>
                                <xsl:when test="fn:string-length($europeana_type) > 0">
                                    <edm:isShownBy>
                                        <xsl:attribute name="rdf:resource">
                                            <xsl:call-template name="generateThumbnailLink">
                                                <xsl:with-param name="role" select="$europeana_type"/>
                                            </xsl:call-template>
                                        </xsl:attribute>
                                    </edm:isShownBy>
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:if test="did/dao[not(@xlink:title='thumbnail')][1]/@xlink:role">
                                        <edm:isShownBy>
                                            <xsl:attribute name="rdf:resource">
                                                <xsl:call-template name="generateThumbnailLink">
                                                    <xsl:with-param name="role" select="did/dao[not(@xlink:title='thumbnail')][1]/@xlink:role"/>
                                                </xsl:call-template>
                                            </xsl:attribute>
                                        </edm:isShownBy>
                                    </xsl:if>
                                </xsl:otherwise>
                            </xsl:choose>
                        </xsl:otherwise>
                    </xsl:choose>
                    <xsl:choose>
                        <xsl:when test="$useExistingRightsInfo='true'">
                            <xsl:choose>
                                <xsl:when test="$currentnode/userestrict[@type='dao']">
                                    <xsl:call-template name="createRights">
                                        <xsl:with-param name="rights" select="$currentnode/userestrict[@type='dao']"/>
                                    </xsl:call-template>
                                </xsl:when>
                                <xsl:when test="$inheritRightsInfo='true' and $inheritedRightsInfo">
                                    <xsl:copy-of select="$inheritedRightsInfo"/>
                                </xsl:when>
                                <xsl:otherwise>
                                    <edm:rights>
                                        <xsl:attribute name="rdf:resource">
                                            <xsl:value-of select="$europeana_rights"/>
                                        </xsl:attribute>
                                    </edm:rights>
                                </xsl:otherwise>
                            </xsl:choose>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:choose>
                                <xsl:when test="$inheritRightsInfo=&quot;true&quot; and $inheritedRightsInfo">
                                    <xsl:copy-of select="$inheritedRightsInfo"/>
                                </xsl:when>
                                <xsl:otherwise>
                                    <edm:rights>
                                        <xsl:attribute name="rdf:resource">
                                            <xsl:value-of select="$europeana_rights"/>
                                        </xsl:attribute>
                                    </edm:rights>
                                </xsl:otherwise>
                            </xsl:choose>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:when>
                <xsl:otherwise>
                    <edm:isShownAt>
                        <xsl:choose>
                            <xsl:when test="@url">
                                <xsl:attribute name="rdf:resource" select="@url"/>
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:attribute name="rdf:resource">
                                    <xsl:choose>
                                        <xsl:when test="$landingPage = 'ape'">
                                            <xsl:choose>
                                                <xsl:when test="$idSource = 'unitid' and did/unitid[@type='call number'] and $isFirstUnitid = 'true'">
                                                    <xsl:value-of select="concat($id_base, normalize-space(/ead/eadheader/eadid), '/unitid/', normalize-space(did/unitid[@type='call number']))"/>
                                                </xsl:when>
                                                <xsl:when test="$idSource = 'cid' and @id">
                                                    <xsl:value-of select="concat($id_base, normalize-space(/ead/eadheader/eadid), '/cid/', @id)"/>
                                                </xsl:when>
                                                <xsl:when test="$mainIdentifier">
                                                    <xsl:value-of select="concat($id_base, normalize-space(/ead/eadheader/eadid), '/position/', $mainIdentifier)"/>
                                                    <!--<xsl:call-template name="number">-->
                                                        <!--<xsl:with-param name="prefix" select="concat($id_base, normalize-space(/ead/eadheader/eadid), '/position/', $mainIdentifier, '-')"/>-->
                                                        <!--<xsl:with-param name="node" select="."/>-->
                                                    <!--</xsl:call-template>-->
                                                </xsl:when>
                                                <xsl:otherwise>
                                                    <xsl:call-template name="number">
                                                        <xsl:with-param name="prefix" select="concat($id_base, normalize-space(/ead/eadheader/eadid), '/position/')"/>
                                                        <xsl:with-param name="node" select="."/>
                                                    </xsl:call-template>
                                                </xsl:otherwise>
                                            </xsl:choose>
                                        </xsl:when>
                                        <xsl:otherwise>
                                            <xsl:value-of select="concat($landingPage, '/', $identifier)"/>
                                        </xsl:otherwise>
                                    </xsl:choose>
                                </xsl:attribute>
                            </xsl:otherwise>
                        </xsl:choose>
                    </edm:isShownAt>
                    <edm:isShownBy>
                        <xsl:attribute name="rdf:resource" select="concat('http://', $host, '/Portal-theme/images/ape/icons/dao_types/europeana/text.png')" />
                    </edm:isShownBy>
                    <edm:rights rdf:resource="http://creativecommons.org/publicdomain/zero/1.0/"/>
                </xsl:otherwise>
            </xsl:choose>
        </ore:Aggregation>
        <edm:ProvidedCHO>
            <xsl:attribute name="rdf:about" select="concat('providedCHO_', $identifier)"/>
            <!--<xsl:if test="/ead/archdesc/did/origination/persname">
                    <xsl:for-each select="/ead/archdesc/did/origination/persname">
                        <dc:creator>
                            <xsl:value-of select="normalize-space(.)"/>
                        </dc:creator>
                    </xsl:for-each>
                </xsl:if>-->
            <xsl:if test="$idSource = 'unitid' and $currentnode/did/unitid[@type='call number']">
                <dc:identifier>
                    <xsl:apply-templates select="$currentnode/did/unitid[@type='call number']"/>
                </dc:identifier>
            </xsl:if>
            <xsl:if test="$idSource = 'cid' and $currentnode/@id">
                <dc:identifier>
                    <xsl:value-of select="$currentnode/@id"/>
                </dc:identifier>
            </xsl:if>
            <!--<xsl:choose>-->
            <!--<xsl:when test="$currentnode/controlaccess">-->
            <!--<xsl:call-template name="controlaccess">-->
            <!--<xsl:with-param name="controlaccesses" select="$currentnode/controlaccess"/>-->
            <!--</xsl:call-template>-->
            <!--</xsl:when>-->
            <!--<xsl:otherwise>-->
            <!--<xsl:if-->
            <!--test="$inheritElementsFromFileLevel = 'true' and fn:string-length($inheritedControlaccesses) > 0">-->
            <!--<xsl:copy-of select="$inheritedControlaccesses"/>-->
            <!--</xsl:if>-->
            <!--</xsl:otherwise>-->
            <!--</xsl:choose>-->
            <xsl:choose>
                <xsl:when test="$currentnode/did/origination">
                    <xsl:call-template name="creator">
                        <xsl:with-param name="originations" select="$currentnode/did/origination"/>
                    </xsl:call-template>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:choose>
                        <xsl:when test="$parentdidnode/origination">
                            <xsl:call-template name="creator">
                                <xsl:with-param name="originations" select="$parentdidnode/origination"/>
                            </xsl:call-template>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:if test="$inheritOrigination = 'true' and fn:string-length($inheritedOriginations) > 0">
                                <xsl:copy-of select="$inheritedOriginations"/>
                            </xsl:if>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:otherwise>
            </xsl:choose>
            <xsl:choose>
                <xsl:when test="$currentnode/did/unitdate">
                    <xsl:apply-templates select="$currentnode/did/unitdate"/>
                </xsl:when>
                <xsl:when test="$inheritFromParent">
                    <xsl:apply-templates select="$parentdidnode/unitdate"/>
                </xsl:when>
            </xsl:choose>
            <xsl:choose>
                <xsl:when test="$currentnode/scopecontent[@encodinganalog='summary']">
                    <xsl:apply-templates select="$currentnode/scopecontent[@encodinganalog='summary']" />
                </xsl:when>
                <xsl:otherwise>
                    <xsl:apply-templates select="$parentcnode/scopecontent[@encodinganalog='summary']" />
                </xsl:otherwise>
            </xsl:choose>
            <xsl:choose>
                <xsl:when test="$currentnode/did/physdesc/physfacet">
                    <dc:format>
                        <xsl:value-of select="$currentnode/did/physdesc/physfacet"/>
                    </dc:format>
                </xsl:when>
                <xsl:when test="$inheritFromParent and $parentdidnode/physdesc/physfacet">
                    <dc:format>
                        <xsl:value-of select="$parentdidnode/physdesc/physfacet"/>
                    </dc:format>
                </xsl:when>
            </xsl:choose>

            <xsl:choose>
                <xsl:when test="$currentnode/did/materialspec">
                    <dc:format>
                        <xsl:value-of select="$currentnode/did/materialspec"/>
                    </dc:format>
                </xsl:when>
                <xsl:when test="$inheritFromParent and $parentdidnode/materialspec">
                    <dc:format>
                        <xsl:value-of select="$parentdidnode/materialspec"/>
                    </dc:format>
                </xsl:when>
            </xsl:choose>
            <xsl:choose>
                <xsl:when test="$currentnode/relatedmaterial">
                    <xsl:apply-templates select="$currentnode/relatedmaterial"/>
                </xsl:when>
                <xsl:when test="$inheritFromParent">
                    <xsl:apply-templates select="$parentcnode/relatedmaterial"/>
                </xsl:when>
            </xsl:choose>
            <dc:type>
                <xsl:choose>
                    <xsl:when test="$currentnode/did/physdesc/genreform">
                        <xsl:value-of select="$currentnode/did/physdesc/genreform"/>
                    </xsl:when>
                    <xsl:when test="$inheritFromParent and $parentdidnode/physdesc/genreform">
                        <xsl:value-of select="$parentdidnode/physdesc/genreform"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:value-of select="'Archival material'"/>
                    </xsl:otherwise>
                </xsl:choose>
            </dc:type>
            <xsl:choose>
                <xsl:when test="$currentnode/did/physdesc/extent">
                    <dcterms:extent>
                        <xsl:value-of select="$currentnode/did/physdesc/extent"/>
                    </dcterms:extent>
                </xsl:when>
                <xsl:when test="$inheritFromParent and $parentdidnode/physdesc/extent">
                    <dcterms:extent>
                        <xsl:value-of select="$parentdidnode/physdesc/extent"/>
                    </dcterms:extent>
                </xsl:when>
            </xsl:choose>
            <xsl:choose>
                <xsl:when test="$currentnode/did/physdesc/dimensions">
                    <dcterms:extent>
                        <xsl:value-of select="$currentnode/did/physdesc/dimensions"/>
                    </dcterms:extent>
                </xsl:when>
                <xsl:when test="$inheritFromParent and $parentdidnode/physdesc/dimensions">
                    <dcterms:extent>
                        <xsl:value-of select="$parentdidnode/physdesc/dimensions"/>
                    </dcterms:extent>
                </xsl:when>
            </xsl:choose>
            <xsl:choose>
                <xsl:when test="$currentnode/otherfindaid">
                    <xsl:apply-templates select="$currentnode/otherfindaid"/>
                </xsl:when>
                <xsl:when test="$inheritFromParent and $parentcnode/otherfindaid">
                    <xsl:apply-templates select="$parentcnode/otherfindaid"/>
                </xsl:when>
            </xsl:choose>
            <xsl:choose>
                <xsl:when test="$currentnode/bibliography/bibref">
                    <xsl:apply-templates select="$currentnode/bibliography/bibref"/>
                </xsl:when>
                <xsl:when test="$inheritFromParent and $parentcnode/bibliography/bibref">
                    <xsl:apply-templates select="$parentcnode/bibliography/bibref"/>
                </xsl:when>
            </xsl:choose>

            <!-- custodhist -->
            <xsl:choose>
                <xsl:when test="$currentnode/custodhist">
                    <xsl:call-template name="custodhist">
                        <xsl:with-param name="custodhists" select="$currentnode/custodhist" />
                    </xsl:call-template>
                </xsl:when>
                <xsl:when test="$inheritFromParent">
                    <xsl:choose>
                        <xsl:when test="$parentcnode/custodhist">
                            <xsl:call-template name="custodhistOnlyOne">
                                <xsl:with-param name="custodhists" select="$parentcnode/custodhist"/>
                            </xsl:call-template>
                            <xsl:if test="(count($parentcnode/custodhist/head) > 1) or (count($parentcnode/custodhist/p) > 1) and $inheritElementsFromFileLevel = 'true'">
                                <dcterms:provenance>
                                    <xsl:attribute name="rdf:resource">
                                        <xsl:choose>
                                            <xsl:when test="/ead/eadheader/eadid/@url">
                                                <xsl:value-of select="/ead/eadheader/eadid/@url"/>
                                            </xsl:when>
                                            <xsl:otherwise>
                                                <xsl:value-of select="concat($id_base, normalize-space(/ead/eadheader/eadid))"/>
                                            </xsl:otherwise>
                                        </xsl:choose>
                                    </xsl:attribute>
                                    <xsl:text>Read more</xsl:text>
                                </dcterms:provenance>
                            </xsl:if>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:if test="$inheritElementsFromFileLevel = 'true' and fn:string-length($inheritedCustodhists) > 0">
                                <xsl:copy-of select="$inheritedCustodhists"/>
                            </xsl:if>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:when>
            </xsl:choose>

            <!--<xsl:choose>
                    <xsl:when test="$currentnode/did/origination">
                        <xsl:call-template name="creator">
                            <xsl:with-param name="originations"
                                            select="$currentnode/did/origination"/>
                        </xsl:call-template>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:if
                            test="$inheritOrigination = 'true' and fn:string-length($inheritedOriginations) > 0">
                            <xsl:copy-of select="$inheritedOriginations"/>
                        </xsl:if>
                    </xsl:otherwise>
                </xsl:choose>-->
            <!--<xsl:choose>
                <xsl:when test="$currentnode/altformavail">
                            <xsl:call-template name="altformavail">
                                <xsl:with-param name="altformavails"
                                    select="$currentnode/altformavail"/>
                            </xsl:call-template>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:choose>
                                <xsl:when test="$parentcnode/altformavail">
                                    <xsl:call-template name="altformavail">
                                        <xsl:with-param name="altformavails"
                                            select="$parentcnode/altformavail"/>
                                    </xsl:call-template>
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:if
                                        test="$inheritElementsFromFileLevel = 'true' and fn:string-length($inheritedAltformavails) > 0">
                                        <xsl:copy-of select="$inheritedAltformavails"/>
                                    </xsl:if>
                                </xsl:otherwise>
                            </xsl:choose>
                        </xsl:otherwise>
            </xsl:choose>-->

            <xsl:choose>
                <xsl:when test="$currentnode/controlaccess">
                    <xsl:call-template name="controlaccess">
                        <xsl:with-param name="controlaccesses" select="$currentnode/controlaccess"/>
                    </xsl:call-template>
                </xsl:when>
                <xsl:when test="$inheritFromParent and $parentcnode/controlaccess">
                    <xsl:call-template name="controlaccess">
                        <xsl:with-param name="controlaccesses" select="$parentcnode/controlaccess"/>
                    </xsl:call-template>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:if test="$inheritElementsFromFileLevel = 'true' and fn:string-length($inheritedControlaccesses) > 0">
                        <xsl:copy-of select="$inheritedControlaccesses"/>
                    </xsl:if>
                </xsl:otherwise>
            </xsl:choose>

            <xsl:choose>
                <xsl:when test="$inheritFromParent and $currentnode/did/unittitle and $parentcnode/did/unittitle and $hasDao">
                    <dc:title>
                        <xsl:value-of select="$parentcnode/did/unittitle[1]"/> >> <xsl:value-of select="$currentnode/did/unittitle"/>
                    </dc:title>
                </xsl:when>
                <xsl:when test="$currentnode/did/unittitle">
                    <dc:title>
                        <xsl:value-of select="$currentnode/did/unittitle"/>
                    </dc:title>
                </xsl:when>
            </xsl:choose>
            <xsl:if test="$currentnode/c">
                <xsl:for-each select="$currentnode/c">
                    <xsl:variable name="currentCPosition">
                        <xsl:call-template name="number">
                            <xsl:with-param name="node" select="."></xsl:with-param>
                        </xsl:call-template>
                    </xsl:variable>
                    <xsl:variable name="isFirstUnitid">
                        <xsl:call-template name="detectFirstUnitid">
                            <xsl:with-param name="positionInDocument" select="$currentCPosition"/>
                            <xsl:with-param name="currentCNode" select="."/>
                        </xsl:call-template>
                    </xsl:variable>
                    <xsl:if test="descendant::dao">
                        <xsl:choose>
                            <xsl:when test="$idSource = 'unitid' and did/unitid[@type='call number'] and $isFirstUnitid = 'true'">
                                <dcterms:hasPart>
                                    <xsl:attribute name="rdf:resource" select="concat('providedCHO_', normalize-space(did/unitid[@type='call number']))"/>
                                </dcterms:hasPart>
                            </xsl:when>
                            <xsl:when test="$idSource = 'cid' and @id">
                                <dcterms:hasPart>
                                    <xsl:attribute name="rdf:resource" select="concat('providedCHO_', normalize-space(@id))"/>
                                </dcterms:hasPart>
                            </xsl:when>
                            <xsl:otherwise>
                                <dcterms:hasPart>
                                    <xsl:attribute name="rdf:resource">
                                        <xsl:call-template name="number">
                                            <xsl:with-param name="prefix">
                                                <xsl:choose>
                                                    <xsl:when test="$mainIdentifier">
                                                        <xsl:value-of select="concat('providedCHO_position_', $mainIdentifier, '-')"/>
                                                    </xsl:when>
                                                    <xsl:otherwise>
                                                        <xsl:value-of select="concat('providedCHO_position_', $positionInDocument, '-')"/>
                                                    </xsl:otherwise>
                                                </xsl:choose>
                                            </xsl:with-param>
                                            <xsl:with-param name="node" select="."/>
                                        </xsl:call-template>
                                    </xsl:attribute>
                                </dcterms:hasPart>
                            </xsl:otherwise>
                        </xsl:choose>
                    </xsl:if>
                </xsl:for-each>
                <!--                            <xsl:for-each select="$currentnode/c/did/unitid[@type='call number']">
                        <xsl:if test="ancestor::node()[2]//dao">
                            <dcterms:hasPart>
                                <xsl:value-of select="concat('providedCHO_', normalize-space(.))"/>
                            </dcterms:hasPart>
                        </xsl:if>
                    </xsl:for-each>-->
            </xsl:if>

            <xsl:choose>
                <xsl:when test="$useExistingLanguage='true'">
                    <xsl:choose>
                        <xsl:when test="$currentnode/did/langmaterial">
                            <xsl:call-template name="language">
                                <xsl:with-param name="langmaterials"
                                    select="$currentnode/did/langmaterial"/>
                            </xsl:call-template>
                        </xsl:when>
                        <xsl:when test="$inheritLanguage = 'true'">
                            <xsl:choose>
                                <xsl:when test="fn:string-length($inheritedLanguages) > 0">
                                    <xsl:copy-of select="$inheritedLanguages"/>
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:for-each select="tokenize($language,' ')">
                                        <dc:language>
                                            <xsl:value-of select="."/>
                                        </dc:language>
                                    </xsl:for-each>
                                </xsl:otherwise>
                            </xsl:choose>
                        </xsl:when>
                        <xsl:when test="$inheritFromParent">
                            <xsl:choose>
                                <xsl:when test="$parentcnode/did/langmaterial">
                                    <xsl:call-template name="language">
                                        <xsl:with-param name="langmaterials"
                                            select="$parentcnode/did/langmaterial"/>
                                    </xsl:call-template>
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:for-each select="tokenize($language,' ')">
                                        <dc:language>
                                            <xsl:value-of select="."/>
                                        </dc:language>
                                    </xsl:for-each>
                                </xsl:otherwise>
                            </xsl:choose>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:choose>
                                <xsl:when test="fn:string-length($language) > 0">
                                    <xsl:for-each select="tokenize($language,' ')">
                                        <dc:language>
                                            <xsl:value-of select="."/>
                                        </dc:language>
                                    </xsl:for-each>
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:for-each select="tokenize($language,' ')">
                                        <dc:language>
                                            <xsl:value-of select="."/>
                                        </dc:language>
                                    </xsl:for-each>
                                </xsl:otherwise>
                            </xsl:choose>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:choose>
                        <xsl:when test="$inheritLanguage = 'true'">
                            <xsl:choose>
                                <xsl:when test="fn:string-length($inheritedLanguages) > 0">
                                    <xsl:copy-of select="$inheritedLanguages"/>
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:for-each select="tokenize($language,' ')">
                                        <dc:language>
                                            <xsl:value-of select="."/>
                                        </dc:language>
                                    </xsl:for-each>
                                </xsl:otherwise>
                            </xsl:choose>
                        </xsl:when>
                        <xsl:when test="$inheritFromParent">
                            <xsl:choose>
                                <xsl:when test="$parentcnode/did/langmaterial">
                                    <xsl:call-template name="language">
                                        <xsl:with-param name="langmaterials"
                                            select="$parentcnode/did/langmaterial"/>
                                    </xsl:call-template>
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:for-each select="tokenize($language,' ')">
                                        <dc:language>
                                            <xsl:value-of select="."/>
                                        </dc:language>
                                    </xsl:for-each>
                                </xsl:otherwise>
                            </xsl:choose>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:choose>
                                <xsl:when test="fn:string-length($language) > 0">
                                    <xsl:for-each select="tokenize($language,' ')">
                                        <dc:language>
                                            <xsl:value-of select="."/>
                                        </dc:language>
                                    </xsl:for-each>
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:for-each select="tokenize($language,' ')">
                                        <dc:language>
                                            <xsl:value-of select="."/>
                                        </dc:language>
                                    </xsl:for-each>
                                </xsl:otherwise>
                            </xsl:choose>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:otherwise>
            </xsl:choose>
            
            <xsl:variable name="positionOfParentInDocument">
                <xsl:call-template name="number">
                    <xsl:with-param name="node" select="$parentcnode"/>
                </xsl:call-template>
            </xsl:variable>
            <xsl:variable name="isParentFirstUnitid">
                <xsl:call-template name="detectFirstUnitid">
                    <xsl:with-param name="positionInDocument" select="$positionOfParentInDocument"/>
                    <xsl:with-param name="currentCNode" select="$parentcnode"/>
                </xsl:call-template>
            </xsl:variable>

            <xsl:variable name="parentparentcnode" select="parent::node()/parent::node()"/>
            <xsl:choose>
                <xsl:when test="local-name($parentparentcnode) = 'archdesc'">
                    <dcterms:isPartOf>
                        <xsl:attribute name="rdf:resource" select="concat('providedCHO_', normalize-space(/ead/eadheader/eadid))"/>
                    </dcterms:isPartOf>
                </xsl:when>
                <xsl:when test="$idSource = 'unitid' and $parentdidnode/unitid[@type='call number'] and $isParentFirstUnitid">
                    <dcterms:isPartOf>
                        <xsl:attribute name="rdf:resource" select="concat('providedCHO_', normalize-space($parentdidnode/unitid[@type='call number']))"/>
                    </dcterms:isPartOf>
                </xsl:when>
                <xsl:when test="$idSource = 'cid' and $parentcnode/@id">
                    <dcterms:isPartOf>
                        <xsl:attribute name="rdf:resource" select="concat('providedCHO_', normalize-space($parentcnode/@id))"/>
                    </dcterms:isPartOf>
                </xsl:when>
                <xsl:otherwise>
                    <dcterms:isPartOf>
                        <xsl:attribute name="rdf:resource">
                            <xsl:choose>
                                <xsl:when test="$positionChain">
                                    <xsl:value-of select="concat('providedCHO_position_', $positionChain)"/>
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:call-template name="number">
                                        <xsl:with-param name="prefix" select="'providedCHO_position_'"/>
                                        <xsl:with-param name="node" select="$parentcnode"/>
                                    </xsl:call-template>
                                </xsl:otherwise>
                            </xsl:choose>
                        </xsl:attribute>
                    </dcterms:isPartOf>
                </xsl:otherwise>
            </xsl:choose>
        <!--<xsl:if test="/ead/archdesc/custodhist">-->
                <!--<dcterms:provenance>-->
                    <!--<xsl:if test="/ead/archdesc/custodhist/head[1]">-->
                        <!--<xsl:value-of-->
                            <!--select="concat(normalize-space(/ead/archdesc/custodhist/head[1]), ': ')"-->
                        <!--/>-->
                    <!--</xsl:if>-->
                    <!--<xsl:value-of select="normalize-space(/ead/archdesc/custodhist/p[1])"/>-->
                <!--</dcterms:provenance>-->
            <!--</xsl:if>-->
            <!--<xsl:if test="(count($parentcnode/custodhist/head) > 1) or (count($parentcnode/custodhist/p) > 1) and $inheritCustodhist eq &quot;true&quot;">-->
                <!--<dcterms:provenance>-->
                    <!--<xsl:attribute name="rdf:resource">-->
                        <!--<xsl:choose>-->
                            <!--<xsl:when test="/ead/eadheader/eadid/@url">-->
                                <!--<xsl:value-of select="/ead/eadheader/eadid/@url"/>-->
                            <!--</xsl:when>-->
                            <!--<xsl:otherwise>-->
                                <!--<xsl:value-of-->
                                    <!--select="concat($id_base, normalize-space(/ead/eadheader/eadid))"/>-->
                            <!--</xsl:otherwise>-->
                        <!--</xsl:choose>-->
                    <!--</xsl:attribute>-->
                    <!--<xsl:text>Read more</xsl:text>-->
                <!--</dcterms:provenance>-->
            <!--</xsl:if>-->
            <xsl:if test="$currentnode/preceding-sibling::c">
                <xsl:variable name="positionOfSiblingInDocument">
                    <xsl:call-template name="number">
                        <xsl:with-param name="node" select="$currentnode/preceding-sibling::c[1]"/>
                    </xsl:call-template>
                </xsl:variable>
                <xsl:variable name="isSiblingFirstUnitid">
                    <xsl:call-template name="detectFirstUnitid">
                        <xsl:with-param name="positionInDocument" select="$positionOfSiblingInDocument"/>
                        <xsl:with-param name="currentCNode" select="$currentnode/preceding-sibling::c[1]"/>
                    </xsl:call-template>
                </xsl:variable>
                <xsl:choose>
                    <xsl:when test="$idSource = 'unitid' and $currentnode/preceding-sibling::*[descendant::did/dao][1]/did/unitid[@type='call number'] and not(key('unitids', $currentnode/preceding-sibling::*[descendant::did/dao][1]/did/unitid[@type='call number'])[2])">
                        <edm:isNextInSequence>
                            <xsl:attribute name="rdf:resource"
                                select="concat('providedCHO_', normalize-space($currentnode/preceding-sibling::*[did/dao][1]/did/unitid[@type='call number']))"
                            />
                        </edm:isNextInSequence>
                    </xsl:when>
                    <xsl:when
                        test="$idSource = 'cid' and $currentnode/preceding-sibling::*[descendant::did/dao][1]/@id">
                        <edm:isNextInSequence>
                            <xsl:attribute name="rdf:resource"
                                select="concat('providedCHO_', normalize-space($currentnode/preceding-sibling::*[did/dao][1]/@id))"
                            />
                        </edm:isNextInSequence>
                    </xsl:when>
                    <xsl:when test="$currentnode/preceding-sibling::*[descendant::did/dao][1]">
                        <edm:isNextInSequence>
                            <xsl:attribute name="rdf:resource">
                                <xsl:call-template name="number">
                                    <xsl:with-param name="prefix">
                                        <xsl:choose>
                                            <xsl:when test="$positionChain">
                                                <xsl:value-of
                                                  select="concat('providedCHO_position_', $positionChain, '-')"
                                                />
                                            </xsl:when>
                                            <xsl:otherwise>
                                                <xsl:value-of select="'providedCHO_position_'"/>
                                            </xsl:otherwise>
                                        </xsl:choose>
                                    </xsl:with-param>
                                    <xsl:with-param name="node"
                                        select="$currentnode/preceding-sibling::*[descendant::did/dao][1]"
                                    />
                                </xsl:call-template>
                            </xsl:attribute>
                        </edm:isNextInSequence>
                    </xsl:when>
                </xsl:choose>
            </xsl:if>
            <xsl:choose>
                <xsl:when test="$hasDao">
                    <xsl:choose>
                        <xsl:when test="$useExistingDaoRole='true'">
                            <xsl:choose>
                                <xsl:when test="did/dao[not(@xlink:title='thumbnail')][1]/@xlink:role">
                                    <edm:type>
                                        <xsl:call-template name="convertToEdmType">
                                            <xsl:with-param name="role"
                                                select="did/dao[not(@xlink:title='thumbnail')][1]/@xlink:role"
                                            />
                                        </xsl:call-template>
                                    </edm:type>
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:if test="fn:string-length($europeana_type) > 0">
                                        <edm:type>
                                            <xsl:call-template name="convertToEdmType">
                                                <xsl:with-param name="role" select="$europeana_type"/>
                                            </xsl:call-template>
                                        </edm:type>
                                    </xsl:if>
                                </xsl:otherwise>
                            </xsl:choose>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:choose>
                                <xsl:when test="fn:string-length($europeana_type) > 0">
                                    <edm:type>
                                        <xsl:call-template name="convertToEdmType">
                                            <xsl:with-param name="role" select="$europeana_type"/>
                                        </xsl:call-template>
                                    </edm:type>
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:if test="did/dao[not(@xlink:title='thumbnail')][1]/@xlink:role">
                                        <edm:type>
                                            <xsl:call-template name="convertToEdmType">
                                                <xsl:with-param name="role"
                                                    select="did/dao[not(@xlink:title='thumbnail')][1]/@xlink:role"
                                                />
                                            </xsl:call-template>
                                            <xsl:value-of select="./@xlink:role"/>
                                        </edm:type>
                                    </xsl:if>
                                </xsl:otherwise>
                            </xsl:choose>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:when>
                <xsl:otherwise>
                    <edm:type>
                        <xsl:value-of select="'TEXT'"/>
                    </edm:type>
                </xsl:otherwise>
            </xsl:choose>
        </edm:ProvidedCHO>
        <xsl:choose>
            <xsl:when test="$hasDao">
                <xsl:apply-templates select="did/dao" mode="webResource">
                    <xsl:with-param name="inheritedRightsInfo" select="$inheritedRightsInfo"/>
                </xsl:apply-templates>
            </xsl:when>
            <xsl:otherwise>
                <edm:WebResource>
                    <xsl:attribute name="rdf:about">
                        <xsl:choose>
                            <xsl:when test="@url">
                                <xsl:value-of select="@url"/>
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:choose>
                                    <xsl:when test="$landingPage = 'ape'">
                                        <xsl:choose>
                                            <xsl:when test="$idSource = 'unitid' and did/unitid[@type='call number'] and $isFirstUnitid = 'true'">
                                                <xsl:value-of select="concat($id_base, normalize-space(/ead/eadheader/eadid), '/unitid/', normalize-space(did/unitid[@type='call number']))"/>
                                            </xsl:when>
                                            <xsl:when test="$idSource = 'cid' and @id">
                                                <xsl:value-of select="concat($id_base, normalize-space(/ead/eadheader/eadid), '/cid/', @id)"/>
                                            </xsl:when>
                                            <xsl:when test="$mainIdentifier">
                                                <xsl:value-of select="concat($id_base, normalize-space(/ead/eadheader/eadid), '/position/', $mainIdentifier)"/>
                                                <!--<xsl:call-template name="number">-->
                                                    <!--<xsl:with-param name="prefix" select="concat($id_base, normalize-space(/ead/eadheader/eadid), '/position/', $positionChain, '-')"/>-->
                                                    <!--<xsl:with-param name="node" select="."/>-->
                                                <!--</xsl:call-template>-->
                                            </xsl:when>
                                            <xsl:otherwise>
                                                <xsl:call-template name="number">
                                                    <xsl:with-param name="prefix" select="concat($id_base, normalize-space(/ead/eadheader/eadid), '/position/')"/>
                                                    <xsl:with-param name="node" select="."/>
                                                </xsl:call-template>
                                            </xsl:otherwise>
                                        </xsl:choose>
                                    </xsl:when>
                                    <xsl:otherwise>
                                        <xsl:value-of select="concat($landingPage, '/', $identifier)"/>
                                    </xsl:otherwise>
                                </xsl:choose>
                            </xsl:otherwise>
                        </xsl:choose>
                    </xsl:attribute>
                    <dc:description>
                        <xsl:choose>
                            <xsl:when test="did/unittitle">
                                <xsl:apply-templates select="did/unittitle" mode="dcDescription"/>
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:value-of select="'Archival material'"/>
                            </xsl:otherwise>
                        </xsl:choose>
                    </dc:description>
                    <edm:rights rdf:resource="http://creativecommons.org/publicdomain/zero/1.0/"/>
                </edm:WebResource>
            </xsl:otherwise>
        </xsl:choose>
        <!--</xsl:for-each>-->
    </xsl:template>

    <xsl:template name="altformavail">
        <xsl:param name="altformavails"/>
        <xsl:for-each select="$altformavails">
            <xsl:variable name="content">
                <xsl:apply-templates/>
            </xsl:variable>
            <dcterms:hasFormat>
                <xsl:value-of select="fn:replace(normalize-space($content), '[\n\t\r]', '')"/>
            </dcterms:hasFormat>
        </xsl:for-each>
    </xsl:template>
    <xsl:template name="controlaccess">
        <xsl:param name="controlaccesses"/>
        <xsl:for-each select="$controlaccesses/persname">
            <dc:coverage>
                <xsl:value-of select="."/>
            </dc:coverage>
        </xsl:for-each>
        <xsl:for-each select="$controlaccesses/corpname">
            <dc:coverage>
                <xsl:value-of select="."/>
            </dc:coverage>
        </xsl:for-each>
        <xsl:for-each select="$controlaccesses/famname">
            <dc:coverage>
                <xsl:value-of select="."/>
            </dc:coverage>
        </xsl:for-each>
        <xsl:for-each select="$controlaccesses/name">
            <dc:coverage>
                <xsl:value-of select="."/>
            </dc:coverage>
        </xsl:for-each>
        <xsl:for-each select="$controlaccesses/geogname">
            <dcterms:spatial>
                <xsl:value-of select="."/>
            </dcterms:spatial>
        </xsl:for-each>
        <xsl:for-each
            select="$controlaccesses/function|$controlaccesses/occupation|$controlaccesses/subject">
            <dc:subject>
                <xsl:value-of select="."/>
            </dc:subject>
        </xsl:for-each>
    </xsl:template>
    <xsl:template name="convertToEdmType">
        <xsl:param name="role"/>
        <xsl:choose>
            <xsl:when test="&quot;TEXT&quot; eq fn:string($role)">
                <xsl:text>TEXT</xsl:text>
            </xsl:when>
            <xsl:when test="&quot;IMAGE&quot; eq fn:string($role)">
                <xsl:text>IMAGE</xsl:text>
            </xsl:when>
            <xsl:when test="&quot;SOUND&quot; eq fn:string($role)">
                <xsl:text>SOUND</xsl:text>
            </xsl:when>
            <xsl:when test="&quot;VIDEO&quot; eq fn:string($role)">
                <xsl:text>VIDEO</xsl:text>
            </xsl:when>
            <xsl:when test="&quot;3D&quot; eq fn:string($role)">
                <xsl:text>3D</xsl:text>
            </xsl:when>
            <xsl:otherwise>
                <xsl:call-template name="convertToEdmType">
                    <xsl:with-param name="role" select="$europeana_type"/>
                </xsl:call-template>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    <xsl:template name="createRights">
        <xsl:param name="rights"/>
        <edm:rights>
            <xsl:attribute name="rdf:resource">
                <xsl:choose>
                    <xsl:when test="$useExistingRightsInfo">
                        <xsl:choose>
                            <xsl:when test="$rights[1]/p[1]/extref/@xlink:href">
                                <xsl:value-of select="$rights[1]/p[1]/extref/@xlink:href"/>
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:value-of select="$europeana_rights"/>
                            </xsl:otherwise>
                        </xsl:choose>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:value-of select="$europeana_rights"/>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:attribute>
        </edm:rights>
    </xsl:template>
    <xsl:template name="creator">
        <xsl:param name="originations"/>
        <xsl:for-each select="$originations">
            <xsl:element name="dc:creator">
                <xsl:for-each select="./corpname|./famname|./name|./persname">
                    <xsl:value-of select="fn:replace(normalize-space(.), '[\n\t\r]', '')"/>
                </xsl:for-each>
                <xsl:for-each select="./text()">
                    <xsl:variable name="text">
                        <xsl:value-of select="fn:replace(normalize-space(.), '[\n\t\r]', '')"/>
                    </xsl:variable>
                    <xsl:if test="fn:string-length($text) > 0">
                        <xsl:value-of select="$text"/>
                    </xsl:if>
                </xsl:for-each>
            </xsl:element>
        </xsl:for-each>
    </xsl:template>
    <xsl:template name="custodhist">
        <xsl:param name="custodhists"/>
        <xsl:for-each select="$custodhists">
            <xsl:variable name="content">
                <xsl:apply-templates/>
            </xsl:variable>
            <dcterms:provenance>
                <xsl:value-of select="fn:replace(normalize-space($content), '[\n\t\r]', ' ')"/>
            </dcterms:provenance>
        </xsl:for-each>
    </xsl:template>
    <xsl:template name="custodhistOnlyOne">
        <xsl:param name="custodhists"/>
        <xsl:for-each select="$custodhists">
            <xsl:variable name="content">
                <xsl:apply-templates select="head[1] | p[1]"/>
            </xsl:variable>
            <dcterms:provenance>
                <xsl:value-of select="fn:replace(normalize-space($content), '[\n\t\r]', ' ')"/>
            </dcterms:provenance>
        </xsl:for-each>
    </xsl:template>
    <xsl:template name="detectFirstUnitid">
        <xsl:param name="positionInDocument"/>
        <xsl:param name="currentCNode"/>
        <xsl:choose>
            <xsl:when test="key('unitids', $currentCNode/did/unitid[@type='call number'])[2]">
                <xsl:variable name="firstElement">
                    <xsl:choose>
                        <xsl:when test="local-name(key('unitids', $currentCNode/did/unitid[@type='call number'])[1]/../..) = 'archdesc'">
                            <xsl:value-of select="'archdesc'"/>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:call-template name="number">
                                <xsl:with-param name="node"
                                    select="key('unitids', $currentCNode/did/unitid[@type='call number'])[1]/../.."/>
                            </xsl:call-template>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:variable>
                <xsl:choose>
                    <xsl:when test="$positionInDocument = $firstElement">
                        <xsl:value-of select="concat($firstElement, ' ', true())"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:value-of select="concat($firstElement, ' ', false())"/>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="true()"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    <xsl:template name="generateThumbnailLink">
        <xsl:param name="role"/>
        <xsl:choose>
            <xsl:when test="&quot;TEXT&quot; eq fn:string($role)">
                <xsl:value-of
                    select="concat('http://', $host, '/Portal-theme/images/ape/icons/dao_types/europeana/text.png')"
                />
            </xsl:when>
            <xsl:when test="&quot;IMAGE&quot; eq fn:string($role)">
                <xsl:value-of
                    select="concat('http://', $host, '/Portal-theme/images/ape/icons/dao_types/europeana/image.png')"
                />
            </xsl:when>
            <xsl:when test="&quot;SOUND&quot; eq fn:string($role)">
                <xsl:value-of
                    select="concat('http://', $host, '/Portal-theme/images/ape/icons/dao_types/europeana/sound.png')"
                />
            </xsl:when>
            <xsl:when test="&quot;VIDEO&quot; eq fn:string($role)">
                <xsl:value-of
                    select="concat('http://', $host, '/Portal-theme/images/ape/icons/dao_types/europeana/video.png')"
                />
            </xsl:when>
            <xsl:when test="&quot;3D&quot; eq fn:string($role)">
                <xsl:value-of
                    select="concat('http://', $host, '/Portal-theme/images/ape/icons/dao_types/europeana/3d.png')"
                />
            </xsl:when>
            <xsl:otherwise>
                <xsl:call-template name="convertToEdmType">
                    <xsl:with-param name="role" select="$europeana_type"/>
                </xsl:call-template>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    <xsl:template name="language">
        <xsl:param name="langmaterials"/>
        <xsl:for-each select="$langmaterials/language/@langcode">
            <xsl:variable name="languageWithoutSpaces">
                <xsl:value-of select="fn:replace(normalize-space(.), '[\n\t\r]', '')"/>
            </xsl:variable>
            <dc:language>
                <xsl:choose>
                    <xsl:when test="fn:string-length($languageWithoutSpaces) > 0">
                        <xsl:value-of select="$languageWithoutSpaces"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:for-each select="tokenize($language,' ')">
                            <dc:language>
                                <xsl:value-of select="."/>
                            </dc:language>
                        </xsl:for-each>
                    </xsl:otherwise>
                </xsl:choose>
            </dc:language>
        </xsl:for-each>
    </xsl:template>
    <xsl:template name="number">
        <xsl:param name="node"/>
        <xsl:param name="prefix"/>
        <xsl:variable name="number">
            <xsl:number count="c" level="single" select="$node[1]"/>
        </xsl:variable>
        <xsl:value-of select="concat($prefix, 'c', $number - 1)"/>
    </xsl:template>
    <xsl:template name="repository">
        <xsl:param name="repository"/>
        <edm:dataProvider>
            <xsl:variable name="content">
                <xsl:apply-templates select="$repository[1]" mode="all-but-address"/>
            </xsl:variable>
            <xsl:value-of select="fn:replace(normalize-space($content), '[\n\t\r]', '')"/>
        </edm:dataProvider>
    </xsl:template>

    <xsl:template match="abbr|emph|expan|extref">
        <xsl:text> </xsl:text>
        <xsl:value-of select="node()"/>
        <xsl:text> </xsl:text>
    </xsl:template>
    <xsl:template match="bibref">
        <xsl:variable name="bibrefContent">
            <xsl:apply-templates mode="bibref-only-nodecontent"/>
        </xsl:variable>
        <dcterms:isReferencedBy>
            <xsl:if test="p/extref/@xlink:href">
                <xsl:attribute name="rdf:resource" select="p/extref/@xlink:href"/>
            </xsl:if>
            <xsl:value-of select="fn:replace(normalize-space($bibrefContent), '[\n\t\r]', '')"/>
        </dcterms:isReferencedBy>
    </xsl:template>
    <xsl:template match="did/dao[not(@xlink:title='thumbnail')]" mode="firstLink">
        <xsl:choose>
            <xsl:when test="@href">
                <edm:isShownAt>
                    <xsl:attribute name="rdf:resource" select="@href"/>
                </edm:isShownAt>
            </xsl:when>
            <xsl:when test="@xlink:href">
                <edm:isShownAt>
                    <xsl:attribute name="rdf:resource" select="@xlink:href"/>
                </edm:isShownAt>
            </xsl:when>
        </xsl:choose>
    </xsl:template>
    <xsl:template match="did/dao[not(@xlink:title='thumbnail')]" mode="additionalLinks">
        <xsl:choose>
            <xsl:when test="@href">
                <edm:hasView>
                    <xsl:attribute name="rdf:resource" select="@href"/>
                </edm:hasView>
            </xsl:when>
            <xsl:when test="@xlink:href">
                <edm:hasView>
                    <xsl:attribute name="rdf:resource" select="@xlink:href"/>
                </edm:hasView>
            </xsl:when>
        </xsl:choose>
    </xsl:template>
    <xsl:template match="did/dao[not(@xlink:title='thumbnail')]" mode="webResource">
        <xsl:param name="inheritedRightsInfo"/>
        <edm:WebResource>
            <xsl:attribute name="rdf:about">
                <xsl:choose>
                    <xsl:when test="@href">
                        <xsl:value-of select="@href"/>
                    </xsl:when>
                    <xsl:when test="@xlink:href">
                        <xsl:value-of select="@xlink:href"/>
                    </xsl:when>
                </xsl:choose>
            </xsl:attribute>
            <dc:description>
                <xsl:choose>
                    <xsl:when test="@title">
                        <xsl:value-of select="@title"/>
                    </xsl:when>
                    <xsl:when test="@xlink:title">
                        <xsl:value-of select="@xlink:title"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:value-of select="'Archival material'"/>
                    </xsl:otherwise>
                </xsl:choose>
            </dc:description>
            <xsl:choose>
                <xsl:when test="$inheritRightsInfo=&quot;true&quot;">
                    <xsl:choose>
                        <xsl:when test="$useExistingRightsInfo=&quot;true&quot;">
                            <xsl:choose>
                                <xsl:when test="current()/../../userestrict[@type='dao']">
                                    <xsl:call-template name="createRights">
                                        <xsl:with-param name="rights" select="current()/../../userestrict[@type='dao']"/>
                                    </xsl:call-template> 
                                </xsl:when>
                                <xsl:when test="$inheritedRightsInfo">
                                    <xsl:copy-of select="$inheritedRightsInfo"/>
                                </xsl:when>
                                <xsl:otherwise>
                                    <edm:rights>
                                        <xsl:attribute name="rdf:resource">
                                            <xsl:value-of select="$europeana_rights"/>
                                        </xsl:attribute>
                                    </edm:rights>
                                </xsl:otherwise>
                            </xsl:choose>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:choose>
                                <xsl:when test="$inheritedRightsInfo">
                                    <xsl:copy-of select="$inheritedRightsInfo"/>
                                </xsl:when>
                                <xsl:otherwise>
                                    <edm:rights>
                                        <xsl:attribute name="rdf:resource">
                                            <xsl:value-of select="$europeana_rights"/>
                                        </xsl:attribute>
                                    </edm:rights>
                                </xsl:otherwise>
                            </xsl:choose>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:when>
                <xsl:when test="$inheritRightsInfo">
                    <xsl:choose>
                        <xsl:when test="$useExistingRightsInfo=&quot;true&quot;">
                            <xsl:choose>
                                <xsl:when test="current()/../../userestrict[@type='dao']">
                                    <xsl:call-template name="createRights">
                                        <xsl:with-param name="rights" select="current()/../../userestrict[@type='dao']"/>
                                    </xsl:call-template> 
                                </xsl:when>
                                <xsl:otherwise>
                                    <edm:rights>
                                        <xsl:attribute name="rdf:resource">
                                            <xsl:value-of select="$europeana_rights"/>
                                        </xsl:attribute>
                                    </edm:rights>
                                </xsl:otherwise>
                            </xsl:choose>
                        </xsl:when>
                        <xsl:otherwise>
                            <edm:rights>
                                <xsl:attribute name="rdf:resource">
                                    <xsl:value-of select="$europeana_rights"/>
                                </xsl:attribute>
                            </edm:rights>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:when>
                <xsl:when test="$inheritRightsInfo=&quot;false&quot;">
                    <xsl:choose>
                        <xsl:when test="$useExistingRightsInfo=&quot;true&quot;">
                            <xsl:choose>
                                <xsl:when test="current()/../../userestrict[@type='dao']">
                                    <xsl:call-template name="createRights">
                                        <xsl:with-param name="rights" select="current()/../../userestrict[@type='dao']"/>
                                    </xsl:call-template> 
                                </xsl:when>
                                <xsl:otherwise>
                                    <edm:rights>
                                        <xsl:attribute name="rdf:resource">
                                            <xsl:value-of select="$europeana_rights"/>
                                        </xsl:attribute>
                                    </edm:rights>
                                </xsl:otherwise>
                            </xsl:choose>
                        </xsl:when>
                        <xsl:otherwise>
                            <edm:rights>
                                <xsl:attribute name="rdf:resource">
                                    <xsl:value-of select="$europeana_rights"/>
                                </xsl:attribute>
                            </edm:rights>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:when>
            </xsl:choose>
        </edm:WebResource>
    </xsl:template>
    <xsl:template match="did/dao[@xlink:title='thumbnail']" mode="thumbnail">
        <xsl:variable name="link">
            <xsl:choose>
                <xsl:when test="@xlink:title='thumbnail'">
                    <xsl:choose>
                        <xsl:when test="@href">
                            <xsl:value-of select="@href"/>
                        </xsl:when>
                        <xsl:when test="@xlink:href">
                            <xsl:value-of select="@xlink:href"/>
                        </xsl:when>
                    </xsl:choose>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:choose>
                        <xsl:when test="fn:string-length($europeana_type) > 0">
                            <xsl:call-template name="generateThumbnailLink">
                                <xsl:with-param name="role" select="$europeana_type"/>
                            </xsl:call-template>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:if test="./@xlink:role">
                                <xsl:call-template name="generateThumbnailLink">
                                    <xsl:with-param name="role" select="./@xlink:role"/>
                                </xsl:call-template>
                            </xsl:if>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <edm:isShownBy>
            <xsl:attribute name="rdf:resource" select="$link"/>
        </edm:isShownBy>
        <edm:object>
            <xsl:attribute name="rdf:resource" select="$link"/>
        </edm:object>
    </xsl:template>
    <xsl:template match="head">
        <xsl:value-of select="node()"/>
        <xsl:text>: </xsl:text>
    </xsl:template>
    <xsl:template match="lb">
        <xsl:text> </xsl:text>
    </xsl:template>
    <xsl:template match="otherfindaid">
        <xsl:if test="p/extref/@xlink:href">
            <dcterms:isReferencedBy>
                <xsl:attribute name="rdf:resource" select="p/extref/@xlink:href"/>
            </dcterms:isReferencedBy>
        </xsl:if>
    </xsl:template>
    <xsl:template match="p">
        <xsl:variable name="content">
            <xsl:apply-templates/>
        </xsl:variable>
        <p>
            <xsl:value-of select="fn:replace(normalize-space($content), '[\n\t\r]', ' ')"/>
        </p>
    </xsl:template>
    <xsl:template match="relatedmaterial">
        <xsl:variable name="content">
            <xsl:apply-templates/>
        </xsl:variable>
        <dc:relation>
            <xsl:value-of select="fn:replace(normalize-space($content), '[\n\t\r]', '')"/>
        </dc:relation>
    </xsl:template>
    <xsl:template match="repository">
        <edm:dataProvider>
            <xsl:variable name="content">
                <xsl:apply-templates mode="all-but-address"/>
            </xsl:variable>
            <xsl:value-of select="fn:replace(normalize-space($content), '[\n\t\r]', '')"/>
        </edm:dataProvider>
    </xsl:template>
    <xsl:template match="scopecontent">
        <xsl:variable name="content">
            <xsl:apply-templates/>
        </xsl:variable>
        <dc:description>
            <xsl:value-of select="fn:replace(normalize-space($content), '[\n\t\r]', '')"/>
        </dc:description>
    </xsl:template>
    <xsl:template match="unitdate">
        <xsl:choose>
            <xsl:when test="$useISODates='true'">
                <xsl:choose>
                    <xsl:when test="./@era=&quot;ce&quot; and ./@normal">
                        <xsl:analyze-string select="./@normal"
                            regex="(\d\d\d\d(-\d\d(-\d\d)?)?)(/(\d\d\d\d(-\d\d(-\d\d)?)?))?">
                            <xsl:matching-substring>
                                <xsl:variable name="startdate">
                                    <xsl:value-of select="regex-group(1)"/>
                                </xsl:variable>
                                <xsl:variable name="enddate">
                                    <xsl:value-of select="regex-group(5)"/>
                                </xsl:variable>
                                <dc:date>
                                    <xsl:if test="fn:string-length($startdate) > 0">
                                        <xsl:value-of select="$startdate"/>
                                    </xsl:if>
                                    <xsl:if
                                        test="fn:string-length($startdate) > 0 and fn:string-length($enddate) > 0">
                                        <xsl:text> - </xsl:text>
                                    </xsl:if>
                                    <xsl:if test="fn:string-length($enddate) > 0">
                                        <xsl:value-of select="$enddate"/>
                                    </xsl:if>
                                </dc:date>
                            </xsl:matching-substring>
                        </xsl:analyze-string>
                        <xsl:if test="./@normal">
                            <dcterms:created>
                                <xsl:value-of select="./@normal"/>
                            </dcterms:created>
                        </xsl:if>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:if test="fn:string-length(normalize-space(.)) > 0">
                            <xsl:variable name="notNormalizedDate">NOT_NORMALIZED_DATE:
                                    <xsl:value-of select="normalize-space(.)"/>
                            </xsl:variable>
                            <xsl:message>
                                <xsl:value-of select="$notNormalizedDate"/>
                            </xsl:message>
                            <dc:date>
                                <xsl:value-of select="$notNormalizedDate"/>
                            </dc:date>
                        </xsl:if>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:when>
            <xsl:otherwise>
                <xsl:if test="fn:string-length(normalize-space(.)) > 0">
                    <dc:date>
                        <xsl:value-of select="normalize-space(.)"/>
                    </dc:date>
                    <xsl:if test="./@normal">
                        <dcterms:created>
                            <xsl:value-of select="./@normal"/>
                        </dcterms:created>
                    </xsl:if>
                </xsl:if>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    <xsl:template match="unitid">
        <xsl:if test="@type ='call number'">
            <xsl:value-of select="fn:replace(normalize-space(.), '[\n\t\r]', '')"/>
        </xsl:if>
    </xsl:template>
    <xsl:template match="unittitle">
        <xsl:variable name="content">
            <xsl:apply-templates/>
        </xsl:variable>
        <dc:title>
            <xsl:value-of select="fn:replace(normalize-space($content), '[\n\t\r]', '')"/>
        </dc:title>
    </xsl:template>
    <xsl:template match="unittitle" mode="dcDescription">
        <xsl:variable name="content">
            <xsl:apply-templates/>
        </xsl:variable>
        <xsl:value-of select="fn:replace(normalize-space($content), '[\n\t\r]', '')"/>
    </xsl:template>
    
    <xsl:template mode="all-but-address" match="address"/>
    <xsl:template mode="bibref-only-nodecontent" match="name|title|imprint"/>

</xsl:stylesheet>
