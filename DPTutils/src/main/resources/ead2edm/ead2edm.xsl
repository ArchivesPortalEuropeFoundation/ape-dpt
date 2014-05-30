<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:edm="http://www.europeana.eu/schemas/edm/"
    xmlns:enrichment="http://www.europeana.eu/schemas/edm/enrichment"
    xmlns:owl="http://www.w3.org/2002/07/owl#"
    xmlns:wgs84="http://www.w3.org/2003/01/geo/wgs84_pos#"
    xmlns:skos="http://www.w3.org/2004/02/skos/core#"
    xmlns:ore="http://www.openarchives.org/ore/terms/"
    xmlns:oai="http://www.openarchives.org/OAI/2.0"
    xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
    xmlns:dcterms="http://purl.org/dc/terms/" xmlns:dc="http://purl.org/dc/elements/1.1/"
    xmlns:europeana="http://www.europeana.eu/schemas/ese/"
    xmlns="http://www.europeana.eu/schemas/edm/"
    xpath-default-namespace="urn:isbn:1-931666-22-9"
    xmlns:fn="http://www.w3.org/2005/xpath-functions" xmlns:xlink="http://www.w3.org/1999/xlink"
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
    <xsl:param name="inheritLanguage"/>
    <xsl:param name="inheritCustodhist"/>
    <xsl:param name="inheritAltformavail"/>
    <xsl:param name="inheritControlaccess"/>
    <xsl:param name="contextInformationPrefix"/>
    <xsl:param name="useExistingDaoRole"/>
    <xsl:param name="useExistingRepository"/>
    <xsl:param name="useExistingLanguage"/>
    <xsl:param name="minimalConversion"/>
    <!-- Params from Ese2Edm -->
    <xsl:param name="edm_identifier"/>
    <xsl:param name="prefix_url"/>
    <xsl:param name="repository_code"/>
    <xsl:param name="xml_type_name"/>

    <xsl:template match="/">
        <rdf:RDF
            xsi:schemaLocation="http://www.w3.org/1999/02/22-rdf-syntax-ns# http://www.europeana.eu/schemas/edm/EDM.xsd">
            <xsl:apply-templates/>
        </rdf:RDF>
    </xsl:template>

    <xsl:template match="eadheader">
        <xsl:apply-templates select="eadid"/>
    </xsl:template>

    <xsl:template match="eadid">
        <ore:Aggregation>
            <xsl:attribute name="rdf:about" select="concat('aggregation_', text())"/>
            <edm:aggregatedCHO>
                <xsl:attribute name="rdf:resource" select="concat('providedCHO_', text())"/>
            </edm:aggregatedCHO>
            <edm:dataProvider>
                <xsl:attribute name="rdf:resource" select="$europeana_dataprovider"/>
            </edm:dataProvider>
            <xsl:if test="@url">
                <edm:isShownAt>
                    <xsl:attribute name="rdf:resource" select="@url"/>
                </edm:isShownAt>
            </xsl:if>
            <edm:provider>
                <xsl:value-of select="$europeana_provider"/>
            </edm:provider>
            <edm:rights>
                <xsl:attribute name="rdf:resource" select="$europeana_rights"/>
            </edm:rights>
        </ore:Aggregation>
        <edm:ProvidedCHO>
            <xsl:attribute name="rdf:about" select="concat('providedCHO_', text())"/>
            <dc:language>
                <xsl:value-of select="$language"/>
            </dc:language>
            <dc:subject>Finding aid</dc:subject>
            <xsl:if test='/ead/eadheader/filedesc/titlestmt/titleproper'>
                <xsl:apply-templates select='/ead/eadheader/filedesc/titlestmt/titleproper'/>
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
                        <xsl:value-of select="'placeholder text'"/>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:attribute>
        </edm:WebResource>
    </xsl:template>

    <xsl:template match="filedesc|profiledesc|revisiondesc"/>

    <xsl:template match="ead/archdesc">
        <xsl:apply-templates select="dsc">
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
            <xsl:with-param name="inheritedControlaccesses">
                <xsl:if test="./controlaccess">
                    <xsl:call-template name="controlaccess">
                        <xsl:with-param name="controlaccesses" select="./controlaccess"/>
                    </xsl:call-template>
                </xsl:if>
            </xsl:with-param>
        </xsl:apply-templates>
    </xsl:template>

    <xsl:template match="dsc">
        <xsl:param name="inheritedOriginations"/>
        <xsl:param name="inheritedLanguages"/>
        <xsl:param name="inheritedControlaccesses"/>
        <xsl:apply-templates select="c">
            <xsl:with-param name="inheritedOriginations" select="$inheritedOriginations"/>
            <xsl:with-param name="inheritedLanguages" select="$inheritedLanguages"/>
            <xsl:with-param name="inheritedControlaccesses" select="$inheritedControlaccesses"/>
        </xsl:apply-templates>
    </xsl:template>

    <xsl:template match="c">
        <xsl:param name="inheritedOriginations"/>
        <xsl:param name="inheritedLanguages"/>
        <xsl:param name="inheritedCustodhists"/>
        <xsl:param name="inheritedAltformavails"/>
        <xsl:param name="inheritedControlaccesses"/>
        <xsl:variable name="updatedInheritedOriginations">
            <xsl:choose>
                <xsl:when test="$inheritOrigination = &quot;true&quot; and ./did/origination">
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
                <xsl:when test="$inheritLanguage = &quot;true&quot; and ./did/langmaterial">
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
                <xsl:when test="$inheritCustodhist = &quot;true&quot; and ./custodhist">
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
                <xsl:when test="$inheritAltformavail = &quot;true&quot; and ./altformavail">
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
                <xsl:when test="$inheritControlaccess = &quot;true&quot; and ./controlaccess">
                    <xsl:call-template name="controlaccess">
                        <xsl:with-param name="controlaccesses" select="./controlaccess"/>
                    </xsl:call-template>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:copy-of select="$inheritedControlaccesses"/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>

        <xsl:if test="count(child::c) > 0">
            <xsl:apply-templates select="c">
                <xsl:with-param name="inheritedOriginations" select="$updatedInheritedOriginations"/>
                <xsl:with-param name="inheritedLanguages" select="$updatedInheritedLanguages"/>
                <xsl:with-param name="inheritedCustodhists" select="$updatedInheritedCustodhists"/>
                <xsl:with-param name="inheritedAltformavails"
                    select="$updatedInheritedAltformavails"/>
                <xsl:with-param name="inheritedControlaccesses"
                    select="$updatedInheritedControlaccesses"/>
            </xsl:apply-templates>
        </xsl:if>
        <xsl:call-template name="addRecord">
            <xsl:with-param name="currentnode" select="."/>
            <xsl:with-param name="inheritedOriginations" select="$updatedInheritedOriginations"/>
            <xsl:with-param name="inheritedLanguages" select="$updatedInheritedLanguages"/>
            <xsl:with-param name="inheritedCustodhists" select="$updatedInheritedCustodhists"/>
            <xsl:with-param name="inheritedAltformavails" select="$updatedInheritedAltformavails"/>
            <xsl:with-param name="inheritedControlaccesses"
                select="$updatedInheritedControlaccesses"/>
        </xsl:call-template>
    </xsl:template>

    <xsl:template name="addRecord">
        <xsl:param name="currentnode"/>
        <xsl:param name="inheritedOriginations"/>
        <xsl:param name="inheritedLanguages"/>
        <xsl:param name="inheritedCustodhists"/>
        <xsl:param name="inheritedAltformavails"/>
        <xsl:param name="inheritedControlaccesses"/>

        <!-- for each dao found, create a set of classes -->
        <xsl:for-each select="did/dao[not(@xlink:title=&quot;thumbnail&quot;)]">
            <!-- VARIABLES -->
            <xsl:variable name="linkPosition" select="position()"/>
            <xsl:variable name="cnode" select="current()/parent::node()/parent::node()"/>
            <xsl:variable name="didnode" select="current()/parent::node()"/>
            <xsl:variable name="parentcnode" select="$cnode/parent::node()"/>
            <xsl:variable name="inheritFromParent"
                select="$cnode[@level=&quot;item&quot;] and $parentcnode[@level=&quot;file&quot;] and $inheritElementsFromFileLevel=&quot;true&quot;"/>
            <xsl:variable name="unitid">
                <xsl:choose>
                    <xsl:when test="$didnode/unitid">
                        <xsl:apply-templates select="$didnode/unitid"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:value-of select="//eadid"/>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:variable>
            <xsl:variable name="parentdidnode" select="$parentcnode/did"/>
            <xsl:variable name="parentofparentcnode" select="$parentcnode/parent::node()"/>

            <!-- ACTUAL CONVERSION BEGINS HERE -->
            <ore:Aggregation>
                <xsl:attribute name="rdf:about" select="concat('aggregation_',$unitid)"/>
                <edm:aggregatedCHO>
                    <xsl:attribute name="rdf:resource" select="concat('providedCHO_',$unitid)"/>
                </edm:aggregatedCHO>
                <xsl:choose>
                    <xsl:when test="$useExistingRepository='true'">
                        <xsl:choose>
                            <xsl:when test="$didnode/repository">
                                <xsl:apply-templates select="$didnode/repository"/>
                            </xsl:when>
                            <xsl:when test="/ead/archdesc/did/repository">
                                <xsl:apply-templates select="/ead/archdesc/did/repository"/>
                            </xsl:when>
                            <xsl:otherwise>
                                <edm:dataProvider>
                                    <xsl:value-of select="$europeana_dataprovider"/>
                                </edm:dataProvider>
                            </xsl:otherwise>
                        </xsl:choose>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:choose>
                            <xsl:when test="$europeana_dataprovider">
                                <edm:dataProvider>
                                    <xsl:value-of select="$europeana_dataprovider"/>
                                </edm:dataProvider>
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:apply-templates select="/ead/archdesc/did/repository"/>
                            </xsl:otherwise>
                        </xsl:choose>
                    </xsl:otherwise>
                </xsl:choose>
                <xsl:if test="/ead/archdesc/did/repository">
                    <xsl:apply-templates select="/ead/archdesc/did/repository"/>
                </xsl:if>
                <xsl:choose>
                    <xsl:when test="@href">
                        <xsl:choose>
                            <xsl:when test="@title = 'thumbnail'">
                                <edm:isShownBy>
                                    <xsl:attribute name="rdf:resource" select="@href"/>
                                </edm:isShownBy>
                            </xsl:when>
                            <xsl:otherwise>
                                <edm:isShownAt>
                                    <xsl:attribute name="rdf:resource" select="@href"/>
                                </edm:isShownAt>
                            </xsl:otherwise>
                        </xsl:choose>
                    </xsl:when>
                    <xsl:when test="@xlink:href">
                        <xsl:choose>
                            <xsl:when test="@xlink:title = 'thumbnail'">
                                <edm:isShownBy>
                                    <xsl:attribute name="rdf:resource" select="@xlink:href"/>
                                </edm:isShownBy>
                            </xsl:when>
                            <xsl:otherwise>
                                <edm:isShownAt>
                                    <xsl:attribute name="rdf:resource" select="@xlink:href"/>
                                </edm:isShownAt>
                            </xsl:otherwise>
                        </xsl:choose>
                    </xsl:when>
                </xsl:choose>
                <edm:provider>
                    <xsl:value-of select="$europeana_provider"/>
                </edm:provider>
                <edm:rights>
                    <xsl:attribute name="rdf:resource" select="$europeana_rights"/>
                </edm:rights>
            </ore:Aggregation>
            <edm:ProvidedCHO>
                <xsl:attribute name="rdf:about" select="concat('providedCHO_', $unitid)"/>
                <dc:identifier>
                    <xsl:apply-templates select="$didnode/unitid"/>
                </dc:identifier>
                <dc:type>
                    <xsl:value-of select="$cnode/@level"></xsl:value-of>
                </dc:type>
                <xsl:choose>
                    <xsl:when test="$cnode/controlaccess">
                        <xsl:call-template name="controlaccess">
                            <xsl:with-param name="controlaccesses" select="$cnode/controlaccess"/>
                        </xsl:call-template>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:if
                            test="$inheritControlaccess = 'true' and fn:string-length($inheritedControlaccesses) > 0">
                            <xsl:copy-of select="$inheritedControlaccesses"/>
                        </xsl:if>
                    </xsl:otherwise>
                </xsl:choose>
                <xsl:choose>
                    <xsl:when test="$inheritFromParent">
                        <dc:title>
                            <xsl:value-of select="$parentcnode/did/unittitle[1]"/> >> <xsl:value-of
                                select="$didnode/unittitle"/>
                        </dc:title>
                        <xsl:choose>
                            <xsl:when test="$didnode/origination">
                                <xsl:call-template name="creator">
                                    <xsl:with-param name="originations"
                                        select="$didnode/origination"/>
                                </xsl:call-template>
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:choose>
                                    <xsl:when test="$parentdidnode/origination">
                                        <xsl:call-template name="creator">
                                            <xsl:with-param name="originations"
                                                select="$parentdidnode/origination"/>
                                        </xsl:call-template>
                                    </xsl:when>
                                    <xsl:otherwise>
                                        <xsl:if
                                            test="$inheritOrigination = 'true' and fn:string-length($inheritedOriginations) > 0">
                                            <xsl:copy-of select="$inheritedOriginations"/>
                                        </xsl:if>
                                    </xsl:otherwise>
                                </xsl:choose>
                            </xsl:otherwise>
                        </xsl:choose>
                        <xsl:choose>
                            <xsl:when test="$didnode/unitdate">
                                <xsl:apply-templates select="$didnode/unitdate"/>
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:apply-templates select="$parentdidnode/unitdate"/>
                            </xsl:otherwise>
                        </xsl:choose>
                        <xsl:choose>
                            <xsl:when
                                test="$cnode/scopecontent[@encodinganalog=&quot;summary&quot;]">
                                <xsl:apply-templates
                                    select="$cnode/scopecontent[@encodinganalog=&quot;summary&quot;]"
                                />
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:apply-templates
                                    select="$parentcnode/scopecontent[@encodinganalog=&quot;summary&quot;]"
                                />
                            </xsl:otherwise>
                        </xsl:choose>
                        <xsl:choose>
                            <xsl:when test="$didnode/physdesc/physfacet">
                                <dc:format>
                                    <xsl:value-of select="$didnode/physdesc/physfacet"/>
                                </dc:format>
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:if test="$parentdidnode/physdesc/physfacet">
                                    <dc:format>
                                        <xsl:value-of select="$parentdidnode/physdesc/physfacet"/>
                                    </dc:format>
                                </xsl:if>
                            </xsl:otherwise>
                        </xsl:choose>
                        <xsl:choose>
                            <xsl:when test="$didnode/materialspec">
                                <dc:format>
                                    <xsl:value-of select="$didnode/materialspec"/>
                                </dc:format>
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:if test="$parentdidnode/materialspec">
                                    <dc:format>
                                        <xsl:value-of select="$parentdidnode/materialspec"/>
                                    </dc:format>
                                </xsl:if>
                            </xsl:otherwise>
                        </xsl:choose>
                        <xsl:choose>
                            <xsl:when test="$cnode/relatedmaterial">
                                <xsl:apply-templates select="$cnode/relatedmaterial"/>
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:apply-templates select="$parentcnode/relatedmaterial"/>
                            </xsl:otherwise>
                        </xsl:choose>
                        <xsl:choose>
                            <xsl:when test="$didnode/physdesc/genreform">
                                <dc:type>
                                    <xsl:value-of select="$didnode/physdesc/genreform"/>
                                </dc:type>
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:if test="$parentdidnode/physdesc/genreform">
                                    <dc:type>
                                        <xsl:value-of select="$parentdidnode/physdesc/genreform"/>
                                    </dc:type>
                                </xsl:if>
                            </xsl:otherwise>
                        </xsl:choose>
                        <xsl:choose>
                            <xsl:when test="$didnode/physdesc/extent">
                                <dcterms:extent>
                                    <xsl:value-of select="$didnode/physdesc/extent"/>
                                </dcterms:extent>
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:if test="$parentdidnode/physdesc/extent">
                                    <dcterms:extent>
                                        <xsl:value-of select="$parentdidnode/physdesc/extent"/>
                                    </dcterms:extent>
                                </xsl:if>
                            </xsl:otherwise>
                        </xsl:choose>
                        <xsl:choose>
                            <xsl:when test="$didnode/physdesc/dimensions">
                                <dcterms:extent>
                                    <xsl:value-of select="$didnode/physdesc/dimensions"/>
                                </dcterms:extent>
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:if test="$parentdidnode/physdesc/dimensions">
                                    <dcterms:extent>
                                        <xsl:value-of select="$parentdidnode/physdesc/dimensions"/>
                                    </dcterms:extent>
                                </xsl:if>
                            </xsl:otherwise>
                        </xsl:choose>
                        <xsl:choose>
                            <xsl:when test="$cnode/otherfindaid">
                                <xsl:apply-templates select="$cnode/otherfindaid"/>
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:if test="$parentcnode/otherfindaid">
                                    <xsl:apply-templates select="$parentcnode/otherfindaid"/>
                                </xsl:if>
                            </xsl:otherwise>
                        </xsl:choose>
                        <xsl:choose>
                            <xsl:when test="$cnode/bibliography/bibref">
                                <xsl:apply-templates select="$cnode/bibliography/bibref"/>
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:if test="$parentcnode/bibliography/bibref">
                                    <xsl:apply-templates select="$parentcnode/bibliography/bibref"/>
                                </xsl:if>
                            </xsl:otherwise>
                        </xsl:choose>
                        <xsl:choose>
                            <xsl:when test="$cnode/custodhist">
                                <xsl:call-template name="custodhist">
                                    <xsl:with-param name="custodhists" select="$cnode/custodhist"/>
                                </xsl:call-template>
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:choose>
                                    <xsl:when test="$parentcnode/custodhist">
                                        <xsl:call-template name="custodhist">
                                            <xsl:with-param name="custodhists"
                                                select="$parentcnode/custodhist"/>
                                        </xsl:call-template>
                                    </xsl:when>
                                    <xsl:otherwise>
                                        <xsl:if
                                            test="$inheritCustodhist = 'true' and fn:string-length($inheritedCustodhists) > 0">
                                            <xsl:copy-of select="$inheritedCustodhists"/>
                                        </xsl:if>
                                    </xsl:otherwise>
                                </xsl:choose>
                            </xsl:otherwise>
                        </xsl:choose>
                        <xsl:choose>
                            <xsl:when test="$cnode/altformavail">
                                <xsl:call-template name="altformavail">
                                    <xsl:with-param name="altformavails"
                                        select="$cnode/altformavail"/>
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
                                            test="$inheritAltformavail = 'true' and fn:string-length($inheritedAltformavails) > 0">
                                            <xsl:copy-of select="$inheritedAltformavails"/>
                                        </xsl:if>
                                    </xsl:otherwise>
                                </xsl:choose>
                            </xsl:otherwise>
                        </xsl:choose>
                        <xsl:choose>
                            <xsl:when test="$cnode/controlaccess">
                                <xsl:call-template name="controlaccess">
                                    <xsl:with-param name="controlaccesses"
                                        select="$cnode/controlaccess"/>
                                    <xsl:with-param name="unitid">
                                        <xsl:if test="$cnode/@level = 'file'">
                                            <xsl:value-of select="$unitid"/>
                                        </xsl:if>
                                    </xsl:with-param>
                                </xsl:call-template>
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:choose>
                                    <xsl:when test="$parentcnode/controlaccess">
                                        <xsl:call-template name="controlaccess">
                                            <xsl:with-param name="controlaccesses"
                                                select="$parentcnode/controlaccess"/>
                                            <xsl:with-param name="unitid">
                                                <xsl:if test="$cnode/@level = 'file'">
                                                  <xsl:value-of select="$unitid"/>
                                                </xsl:if>
                                            </xsl:with-param>
                                        </xsl:call-template>
                                    </xsl:when>
                                    <xsl:otherwise>
                                        <xsl:if
                                            test="$inheritControlaccess = 'true' and fn:string-length($inheritedControlaccesses) > 0">
                                            <xsl:copy-of select="$inheritedControlaccesses"/>
                                        </xsl:if>
                                    </xsl:otherwise>
                                </xsl:choose>
                            </xsl:otherwise>
                        </xsl:choose>

                    </xsl:when>
                    <xsl:otherwise>
                        <dc:title>
                            <xsl:value-of select="$didnode/unittitle"/>
                        </dc:title>
                        <xsl:choose>
                            <xsl:when test="$didnode/origination">
                                <xsl:call-template name="creator">
                                    <xsl:with-param name="originations"
                                        select="$didnode/origination"/>
                                </xsl:call-template>
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:if
                                    test="$inheritOrigination = 'true' and fn:string-length($inheritedOriginations) > 0">
                                    <xsl:copy-of select="$inheritedOriginations"/>
                                </xsl:if>
                            </xsl:otherwise>
                        </xsl:choose>
                        <xsl:apply-templates select="$didnode/unitdate"/>
                        <xsl:choose>
                            <xsl:when
                                test="$cnode/scopecontent[@encodinganalog=&quot;summary&quot;]">
                                <xsl:apply-templates
                                    select="$cnode/scopecontent[@encodinganalog=&quot;summary&quot;]"
                                />
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:apply-templates
                                    select="$parentcnode/scopecontent[@encodinganalog=&quot;summary&quot;]"
                                />
                            </xsl:otherwise>
                        </xsl:choose>
                        <xsl:if test="$didnode/physdesc/physfacet">
                            <dc:format>
                                <xsl:value-of select="$didnode/physdesc/physfacet"/>
                            </dc:format>
                        </xsl:if>
                        <xsl:if test="$didnode/materialspec">
                            <dc:format>
                                <xsl:value-of select="$didnode/materialspec"/>
                            </dc:format>
                        </xsl:if>
                        <xsl:choose>
                            <xsl:when test="$cnode/relatedmaterial">
                                <xsl:apply-templates select="$cnode/relatedmaterial"/>
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:apply-templates select="$parentcnode/relatedmaterial"/>
                            </xsl:otherwise>
                        </xsl:choose>
                        <xsl:if test="$didnode/physdesc/genreform">
                            <dc:type>
                                <xsl:value-of select="$didnode/physdesc/genreform"/>
                            </dc:type>
                        </xsl:if>
                        <xsl:if test="$didnode/physdesc/extent">
                            <dcterms:extent>
                                <xsl:value-of select="$didnode/physdesc/extent"/>
                            </dcterms:extent>
                        </xsl:if>
                        <xsl:if test="$didnode/physdesc/dimensions">
                            <dcterms:extent>
                                <xsl:value-of select="$didnode/physdesc/dimensions"/>
                            </dcterms:extent>
                        </xsl:if>
                        <xsl:if test="$cnode/otherfindaid">
                            <xsl:apply-templates select="$cnode/otherfindaid"/>
                        </xsl:if>
                        <xsl:if test="$cnode/bibliography/bibref">
                            <xsl:apply-templates select="$cnode/bibliography/bibref"/>
                        </xsl:if>
                        <xsl:choose>
                            <xsl:when test="$didnode/origination">
                                <xsl:call-template name="creator">
                                    <xsl:with-param name="originations"
                                        select="$didnode/origination"/>
                                </xsl:call-template>
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:if
                                    test="$inheritOrigination = 'true' and fn:string-length($inheritedOriginations) > 0">
                                    <xsl:copy-of select="$inheritedOriginations"/>
                                </xsl:if>
                            </xsl:otherwise>
                        </xsl:choose>
                        <xsl:choose>
                            <xsl:when test="$cnode/custodhist">
                                <xsl:call-template name="custodhist">
                                    <xsl:with-param name="custodhists" select="$cnode/custodhist"/>
                                </xsl:call-template>
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:if
                                    test="$inheritCustodhist = 'true' and fn:string-length($inheritedCustodhists) > 0">
                                    <xsl:copy-of select="$inheritedCustodhists"/>
                                </xsl:if>
                            </xsl:otherwise>
                        </xsl:choose>
                        <xsl:choose>
                            <xsl:when test="$cnode/altformavail">
                                <xsl:call-template name="altformavail">
                                    <xsl:with-param name="altformavails"
                                        select="$cnode/altformavail"/>
                                </xsl:call-template>
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:if
                                    test="$inheritAltformavail = 'true' and fn:string-length($inheritedAltformavails) > 0">
                                    <xsl:copy-of select="$inheritedAltformavails"/>
                                </xsl:if>
                            </xsl:otherwise>
                        </xsl:choose>
                        <xsl:choose>
                            <xsl:when test="$cnode/controlaccess">
                                <xsl:call-template name="controlaccess">
                                    <xsl:with-param name="controlaccesses"
                                        select="$cnode/controlaccess"/>
                                    <xsl:with-param name="unitid">
                                        <xsl:if test="$cnode/@level = 'file'">
                                            <xsl:value-of select="$unitid"/>
                                        </xsl:if>
                                    </xsl:with-param>
                                </xsl:call-template>
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:if
                                    test="$inheritControlaccess = 'true' and fn:string-length($inheritedControlaccesses) > 0">
                                    <xsl:copy-of select="$inheritedControlaccesses"/>
                                </xsl:if>
                            </xsl:otherwise>
                        </xsl:choose>
                    </xsl:otherwise>
                </xsl:choose>
                <xsl:choose>
                    <xsl:when test="$useExistingLanguage='true'">
                        <xsl:choose>
                            <xsl:when test="$didnode/langmaterial">
                                <xsl:call-template name="language">
                                    <xsl:with-param name="langmaterials"
                                        select="$didnode/langmaterial"/>
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
                <xsl:if test="$parentdidnode/unitid">
                    <dcterms:isPartOf>
                        <xsl:attribute name="rdf:resource">
                            <xsl:value-of select="$parentdidnode/unitid"/>
                        </xsl:attribute>
                    </dcterms:isPartOf>
                </xsl:if>
                <xsl:choose>
                    <xsl:when test="$useExistingDaoRole='true'">
                        <xsl:choose>
                            <xsl:when test="./@xlink:role">
                                <edm:type>
                                    <xsl:call-template name="convertToEdmType">
                                        <xsl:with-param name="role" select="./@xlink:role"></xsl:with-param>
                                    </xsl:call-template>
                                </edm:type>
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:if test="fn:string-length($europeana_type) > 0">
                                    <edm:type>
                                        <xsl:call-template name="convertToEdmType">
                                            <xsl:with-param name="role" select="$europeana_type"></xsl:with-param>
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
                                        <xsl:with-param name="role" select="$europeana_type"></xsl:with-param>
                                    </xsl:call-template>
                                </edm:type>
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:if test="./@xlink:role">
                                    <edm:type>
                                        <xsl:call-template name="convertToEdmType">
                                            <xsl:with-param name="role" select="./@xlink:role"></xsl:with-param>
                                        </xsl:call-template><xsl:value-of select="./@xlink:role"/>
                                    </edm:type>
                                </xsl:if>
                            </xsl:otherwise>
                        </xsl:choose>
                    </xsl:otherwise>
                </xsl:choose>
            </edm:ProvidedCHO>
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
                <xsl:if test="@title">
                    <dc:description>
                        <xsl:value-of select="@title"/>
                    </dc:description>
                </xsl:if>
                <xsl:if test="@xlink:title">
                    <dc:description>
                        <xsl:value-of select="@xlink:title"/>
                    </dc:description>
                </xsl:if>
            </edm:WebResource>
        </xsl:for-each>
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
        <xsl:param name="unitid"/>
        <xsl:for-each select="$controlaccesses">
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
                    <xsl:if test="$unitid">
                        <xsl:attribute name="rdf:resource" select="$unitid"/>
                    </xsl:if>
                    <xsl:value-of select="."/>
                </dc:subject>
            </xsl:for-each>
        </xsl:for-each>
    </xsl:template>
    <xsl:template name="convertToEdmType">
        <xsl:param name="role"/>
        <xsl:choose>
            <xsl:when test=' "TEXT" eq fn:string($role)'>
                <xsl:text>TEXT</xsl:text>
            </xsl:when>
            <xsl:when test=' "IMAGE" eq fn:string($role)'>
                <xsl:text>IMAGE</xsl:text>
            </xsl:when>
            <xsl:when test=' "SOUND" eq fn:string($role)'>
                <xsl:text>SOUND</xsl:text>
            </xsl:when>
            <xsl:when test=' "VIDEO" eq fn:string($role)'>
                <xsl:text>VIDEO</xsl:text>
            </xsl:when>
            <xsl:when test=' "3D" eq fn:string($role)'>
                <xsl:text>3D</xsl:text>
            </xsl:when>
            <xsl:otherwise>
                <xsl:call-template name="convertToEdmType">
                    <xsl:with-param name="role" select="$europeana_type"></xsl:with-param>
                </xsl:call-template>
            </xsl:otherwise>
        </xsl:choose>
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
                <xsl:value-of select="fn:replace(normalize-space($content), '[\n\t\r]', '')"/>
            </dcterms:provenance>
        </xsl:for-each>
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
    <xsl:template match='titleproper'>
        <xsl:variable name="content">
            <xsl:apply-templates />
        </xsl:variable>
        <dc:title>
            <xsl:value-of select="fn:replace(normalize-space($content), '[\n\t\r]', '')"/>
        </dc:title>
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

    <xsl:template mode="all-but-address" match="address"/>
    <xsl:template mode="bibref-only-nodecontent" match="name|title|imprint"/>

</xsl:stylesheet>
