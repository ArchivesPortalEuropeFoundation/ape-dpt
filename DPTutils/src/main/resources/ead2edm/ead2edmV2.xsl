<?xml version="1.0" encoding="UTF-8"?>
<!--
  #%L
  Data Preparation Tool Standalone mapping tool
  %%
  Copyright (C) 2009 - 2015 Archives Portal Europe
  %%
  Licensed under the EUPL, Version 1.1 or - as soon they will be approved by the European Commission - subsequent versions of the EUPL (the "Licence");
  You may not use this work except in compliance with the Licence.
  You may obtain a copy of the Licence at:

  http://ec.europa.eu/idabc/eupl5

  Unless required by applicable law or agreed to in writing, software distributed under the Licence is distributed on an "AS IS" basis,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the Licence for the specific language governing permissions and limitations under the Licence.
  #L%
  -->
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format"
                xmlns:edm="http://www.europeana.eu/schemas/edm/"
                xmlns:owl="http://www.w3.org/2002/07/owl#"
                xmlns:skos="http://www.w3.org/2004/02/skos/core#"
                xmlns:ore="http://www.openarchives.org/ore/terms/"
                xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
                xmlns:dcterms="http://purl.org/dc/terms/"
                xmlns:dc="http://purl.org/dc/elements/1.1/"
                xmlns:fn="http://www.w3.org/2005/xpath-functions"
                xmlns:xlink="http://www.w3.org/1999/xlink"
                xmlns:svcs="http://rdfs.org/sioc/services#"
                xmlns:doap="http://usefulinc.com/ns/doap#"
                xpath-default-namespace="urn:isbn:1-931666-22-9"
                exclude-result-prefixes="xlink fo fn">
    <xsl:strip-space elements="*"/>
    <xsl:output method="xml" encoding="UTF-8" indent="yes"/>

    <!-- Params from Ead2Ese -->
    <xsl:param name="europeana_provider"/>
    <xsl:param name="europeana_dataprovider"/>
    <xsl:param name="europeana_rights"/>
    <xsl:param name="dc_rights"/>
    <xsl:param name="europeana_type"/>
    <xsl:param name="useISODates"/>
    <xsl:param name="languageMaterial"/>
    <xsl:param name="languageDescription"/>
    <xsl:param name="inheritUnittitle"/>
    <xsl:param name="useExistingDaoRole"/>
    <xsl:param name="useExistingRepository"/>
    <xsl:param name="useExistingLanguageMaterial"/>
    <xsl:param name="useExistingLanguageDescription"/>
    <xsl:param name="languageDescriptionSameAsLanguageMaterial"/>
    <xsl:param name="useExistingRightsInfo"/>
    <xsl:param name="idSource"/>
    <xsl:param name="landingPage"/>
    <xsl:param name="useArchUnittitle"/>
    <xsl:param name="outputBaseDirectory"/>
    <!-- Params from Ese2Edm -->
    <xsl:param name="edm_identifier"/>
    <xsl:param name="host"/>
    <xsl:param name="repository_code"/>
    <xsl:param name="xml_type_name"/>

    <!-- Variables -->
    <xsl:variable name="id_base"
                  select="concat('https://', $host, '/archive/aicode/' , $repository_code, '/type/', $xml_type_name, '/id/')"/>
    <!--<xsl:variable name="eadidEncoded" select="encode-for-uri(/ead/eadheader/eadid)"/>-->
    <xsl:variable name="eadidEncoded">
        <xsl:call-template name="simpleReplace">
            <xsl:with-param name="input" select="normalize-space(encode-for-uri(/ead/eadheader/eadid))"/>
        </xsl:call-template>
    </xsl:variable>
    <!--<xsl:variable name="eadidFilenameEncoded" select="encode-for-uri(/ead/eadheader/eadid)"/>-->
    <xsl:variable name="eadidFilenameEncoded">
        <xsl:call-template name="filenameReplace">
            <xsl:with-param name="input" select="normalize-space(encode-for-uri(/ead/eadheader/eadid))"/>
        </xsl:call-template>
    </xsl:variable>
    <xsl:variable name="languageUsedForDescription">
        <xsl:choose>
            <xsl:when test="$useExistingLanguageDescription = true()">
                <xsl:choose>
                    <xsl:when test="/ead/eadheader/profiledesc/langusage/language[@langcode != '']">
                        <xsl:value-of select="/ead/eadheader/profiledesc/langusage/language[@langcode != ''][1]/@langcode"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:value-of select="$languageDescription"/>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="$languageDescription"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:variable>
    <!-- Key for detection of unitid duplicates -->
    <xsl:key name="unitids" match="unitid" use="text()"></xsl:key>

    <xsl:template match="/">
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="eadheader">
        <xsl:apply-templates select="eadid"/>
    </xsl:template>

    <xsl:template match="eadid">
        <xsl:result-document href="{$outputBaseDirectory}/{$eadidFilenameEncoded}/{$eadidFilenameEncoded}.xml" xpath-default-namespace="urn:isbn:1-931666-22-9" >
            <rdf:RDF xmlns:adms="http://www.w3.org/ns/adms#"
                     xmlns:cc="http://creativecommons.org/ns#"
                     xmlns:crm="http://www.cidoc-crm.org/rdfs/cidoc_crm_v5.0.2_english_label.rdfs#"
                     xmlns:dc="http://purl.org/dc/elements/1.1/" xmlns:dcat="http://www.w3.org/ns/dcat#"
                     xmlns:dcterms="http://purl.org/dc/terms/" xmlns:doap="http://usefulinc.com/ns/doap#"
                     xmlns:ebucore="http://www.ebu.ch/metadata/ontologies/ebucore/ebucore#"
                     xmlns:edm="http://www.europeana.eu/schemas/edm/"
                     xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:odrl="http://www.w3.org/ns/odrl/2/"
                     xmlns:ore="http://www.openarchives.org/ore/terms/"
                     xmlns:owl="http://www.w3.org/2002/07/owl#"
                     xmlns:rdaGr2="http://rdvocab.info/ElementsGr2/"
                     xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
                     xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#"
                     xmlns:skos="http://www.w3.org/2004/02/skos/core#"
                     xmlns:svcs="http://rdfs.org/sioc/services#"
                     xmlns:wgs84_pos="http://www.w3.org/2003/01/geo/wgs84_pos#">
                <ore:Aggregation>
                    <xsl:attribute name="rdf:about" select="concat('aggregation_', .)"/>
                    <edm:aggregatedCHO>
                        <xsl:attribute name="rdf:resource" select="concat('providedCHO_', .)"/>
                    </edm:aggregatedCHO>
                    <xsl:choose>
                        <xsl:when test="$useExistingRepository = &quot;true&quot;">
                            <xsl:choose>
                                <xsl:when
                                        test="/ead/archdesc/did/repository[descendant-or-self::text() != '']">
                                    <xsl:call-template name="repository">
                                        <xsl:with-param name="repository"
                                                        select="/ead/archdesc/did/repository[descendant-or-self::text() != '']"
                                        />
                                    </xsl:call-template>
                                </xsl:when>
                                <xsl:otherwise>
                                    <edm:dataProvider>
                                        <xsl:value-of
                                                select="normalize-space($europeana_dataprovider)"/>
                                    </edm:dataProvider>
                                </xsl:otherwise>
                            </xsl:choose>
                        </xsl:when>
                        <xsl:otherwise>
                            <edm:dataProvider>
                                <xsl:value-of select="normalize-space($europeana_dataprovider)"/>
                            </edm:dataProvider>
                        </xsl:otherwise>
                    </xsl:choose>
                    <edm:isShownAt>
                        <xsl:choose>
                            <xsl:when test="@url != ''">
                                <xsl:attribute name="rdf:resource" select="@url"/>
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:choose>
                                    <xsl:when test="$landingPage = 'ape'">
                                        <xsl:attribute name="rdf:resource"
                                                       select="concat($id_base, $eadidEncoded)"/>
                                    </xsl:when>
                                    <xsl:otherwise>
                                        <xsl:attribute name="rdf:resource"
                                                       select="normalize-space($landingPage)"/>
                                    </xsl:otherwise>
                                </xsl:choose>
                            </xsl:otherwise>
                        </xsl:choose>
                    </edm:isShownAt>
                    <edm:object>
                        <xsl:attribute name="rdf:resource"
                                       select="concat('http://', $host, '/Portal-theme/images/ape/icons/dao_types/europeana/text.png')"
                        />
                    </edm:object>
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
                    <xsl:if
                            test="/ead/archdesc/did/origination[text() != ''] or count(/ead/archdesc/did/origination/*) > 0">
                        <xsl:call-template name="creator">
                            <xsl:with-param name="originations"
                                            select="/ead/archdesc/did/origination"/>
                        </xsl:call-template>
                    </xsl:if>
                    <xsl:if test="/ead/archdesc/scopecontent[@encodinganalog = 'summary']">
                        <xsl:apply-templates
                                select="/ead/archdesc/scopecontent[@encodinganalog = 'summary']"/>
                    </xsl:if>
                    <xsl:if test="/ead/archdesc/did/unitid[text() != '']">
                        <dc:identifier>
                            <xsl:value-of select="/ead/archdesc/did/unitid[text() != '']"/>
                        </dc:identifier>
                    </xsl:if>
                    <xsl:choose>
                        <xsl:when test="$useExistingLanguageMaterial = 'true'">
                            <xsl:choose>
                                <xsl:when test="/ead/archdesc/did/langmaterial">
                                    <xsl:call-template name="language">
                                        <xsl:with-param name="langmaterials"
                                                        select="/ead/archdesc/did/langmaterial"/>
                                    </xsl:call-template>
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:for-each select="tokenize($languageMaterial, ' ')">
                                        <dc:language>
                                            <xsl:value-of select="."/>
                                        </dc:language>
                                    </xsl:for-each>
                                </xsl:otherwise>
                            </xsl:choose>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:for-each select="tokenize($languageMaterial, ' ')">
                                <dc:language>
                                    <xsl:value-of select="."/>
                                </dc:language>
                            </xsl:for-each>
                        </xsl:otherwise>
                    </xsl:choose>

                    <xsl:choose>
                        <xsl:when test="$useArchUnittitle = true()">
                            <xsl:choose>
                                <xsl:when test="/ead/archdesc/did/unittitle != ''">
                                    <dc:title>
                                        <xsl:attribute name="xml:lang" select="$languageUsedForDescription"/>
                                        <xsl:value-of select="/ead/archdesc/did/unittitle"/>
                                    </dc:title>
                                </xsl:when>
                                <xsl:when test="/ead/eadheader/filedesc/titlestmt/titleproper != ''">
                                    <dc:title>
                                        <xsl:attribute name="xml:lang" select="$languageUsedForDescription"/>
                                        <xsl:value-of select="/ead/eadheader/filedesc/titlestmt/titleproper"/>
                                    </dc:title>
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:if
                                            test="not(/ead/archdesc/scopecontent[@encodinganalog = 'summary'] != '')">
                                        <dc:title>
                                            <xsl:attribute name="xml:lang" select="$languageUsedForDescription"/>
                                            <xsl:text>No title</xsl:text>
                                        </dc:title>
                                    </xsl:if>
                                </xsl:otherwise>
                            </xsl:choose>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:choose>
                                <xsl:when test="/ead/eadheader/filedesc/titlestmt/titleproper != ''">
                                    <dc:title>
                                        <xsl:attribute name="xml:lang" select="$languageUsedForDescription"/>
                                        <xsl:value-of select="/ead/eadheader/filedesc/titlestmt/titleproper"/>
                                    </dc:title>
                                </xsl:when>
                                <xsl:when test="/ead/archdesc/did/unittitle != ''">
                                    <dc:title>
                                        <xsl:attribute name="xml:lang" select="$languageUsedForDescription"/>
                                        <xsl:value-of select="/ead/archdesc/did/unittitle"/>
                                    </dc:title>
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:if
                                            test="not(/ead/archdesc/scopecontent[@encodinganalog = 'summary'] != '')">
                                        <dc:title>
                                            <xsl:attribute name="xml:lang" select="$languageUsedForDescription"/>
                                            <xsl:text>No title</xsl:text>
                                        </dc:title>
                                    </xsl:if>
                                </xsl:otherwise>
                            </xsl:choose>
                        </xsl:otherwise>
                    </xsl:choose>

                    <xsl:if test="/ead/archdesc/relatedmaterial[text() != '']">
                        <xsl:apply-templates select="/ead/archdesc/relatedmaterial[text() != '']"/>
                    </xsl:if>
                    <xsl:if test="/ead/archdesc/custodhist[descendant::text() != '']">
                        <xsl:call-template name="custodhist">
                            <xsl:with-param name="custodhists"
                                            select="/ead/archdesc/custodhist[descendant::text() != '']"/>
                        </xsl:call-template>
                    </xsl:if>
                    <xsl:if
                            test="/ead/archdesc/bibliography/bibref[text() != '']">
                        <xsl:for-each select="/ead/archdesc/bibliography/bibref[text() != '']">
                            <xsl:call-template name="bibref">
                                <xsl:with-param name="bibrefs" select="."/>
                            </xsl:call-template>
                        </xsl:for-each>
                    </xsl:if>
                    <xsl:if
                            test="/ead/archdesc/bibliography/p[text() != '']">
                        <xsl:for-each select="/ead/archdesc/bibliography/p[text() != '']">
                            <xsl:call-template name="bibref">
                                <xsl:with-param name="bibrefs" select="."/>
                            </xsl:call-template>
                        </xsl:for-each>
                    </xsl:if>
                    <xsl:if test="/ead/archdesc/controlaccess">
                        <xsl:call-template name="controlaccess">
                            <xsl:with-param name="controlaccesses"
                                            select="/ead/archdesc/controlaccess"/>
                        </xsl:call-template>
                    </xsl:if>
                    <xsl:if test="/ead/archdesc/did/materialspec[text() != '']">
                        <xsl:call-template name="materialspec">
                            <xsl:with-param name="materialspecs" select="/ead/archdesc/did/materialspec[text() != '']"/>
                        </xsl:call-template>
                    </xsl:if>
                    <xsl:if test="/ead/archdesc/altformavail[text() != '']">
                        <xsl:call-template name="altformavail">
                            <xsl:with-param name="altformavails"
                                            select="/ead/archdesc/altformavail[text() != '']"/>
                        </xsl:call-template>
                    </xsl:if>
                    <dc:type>
                        <xsl:attribute name="rdf:resource">
                            <xsl:text>http://vocab.getty.edu/aat/300379505</xsl:text>
                        </xsl:attribute>
                    </dc:type>
                    <xsl:if test="/ead/archdesc/did/physdesc/genreform[text() != '']">
                        <dc:type>
                            <xsl:attribute name="xml:lang" select="$languageUsedForDescription"/>
                            <xsl:value-of select="/ead/archdesc/did/physdesc/genreform[text() != '']"/>
                        </dc:type>
                    </xsl:if>
                    <xsl:if
                            test="/ead/archdesc/did/physdesc/physfacet[text() != '']">
                        <dc:format>
                            <xsl:attribute name="xml:lang" select="$languageUsedForDescription"/>
                            <xsl:value-of select="/ead/archdesc/did/physdesc/physfacet[text() != '']"/>
                        </dc:format>
                    </xsl:if>
                    <xsl:if
                            test="/ead/archdesc/did/physdesc/extent[text() != '']">
                        <dcterms:extent>
                            <xsl:value-of select="/ead/archdesc/did/physdesc/extent[text() != '']"/>
                        </dcterms:extent>
                    </xsl:if>
                    <xsl:if
                            test="/ead/archdesc/did/physdesc/dimensions[text() != '']">
                        <dcterms:extent>
                            <xsl:value-of
                                    select="/ead/archdesc/did/physdesc/dimensions[text() != '']"/>
                        </dcterms:extent>
                    </xsl:if>
                    <xsl:if
                            test="/ead/archdesc/dsc/descendant::dao[normalize-space(@xlink:href) != '' and not(@xlink:title='thumbnail') and not(@xlink:title='manifest') and not(@xlink:title='service')]">
                        <xsl:for-each
                                select="/ead/archdesc/dsc/descendant::c[did/dao[normalize-space(@xlink:href) != '' and not(@xlink:title='thumbnail') and not(@xlink:title='manifest') and not(@xlink:title='service')]]">
                            <xsl:variable name="currentCPosition">
                                <xsl:call-template name="number">
                                    <xsl:with-param name="node" select="."/>
                                </xsl:call-template>
                            </xsl:variable>
                            <xsl:variable name="isFirstUnitid">
                                <xsl:call-template name="detectFirstUnitid">
                                    <xsl:with-param name="positionInDocument"
                                                    select="$currentCPosition"/>
                                    <xsl:with-param name="currentCNode" select="."/>
                                </xsl:call-template>
                            </xsl:variable>
                            <xsl:choose>
                                <xsl:when
                                        test="$idSource = 'unitid' and did/unitid[text() != '' and @type = 'call number'] and $isFirstUnitid = 'true'">
                                    <dcterms:hasPart>
                                        <xsl:attribute name="rdf:resource"
                                                       select="concat('providedCHO_', normalize-space(/ead/eadheader/eadid), '_', normalize-space(did/unitid[text() != '' and @type = 'call number'][1]))"
                                        />
                                    </dcterms:hasPart>
                                </xsl:when>
                                <xsl:when test="$idSource = 'cid' and @id">
                                    <dcterms:hasPart>
                                        <xsl:attribute name="rdf:resource"
                                                       select="concat('providedCHO_', normalize-space(/ead/eadheader/eadid), '_', normalize-space(@id))"
                                        />
                                    </dcterms:hasPart>
                                </xsl:when>
                                <xsl:otherwise>
                                    <dcterms:hasPart>
                                        <xsl:attribute name="rdf:resource">
                                            <xsl:call-template name="positionForHasPart">
                                                <xsl:with-param name="node" select="."/>
                                            </xsl:call-template>
                                        </xsl:attribute>
                                    </dcterms:hasPart>
                                </xsl:otherwise>
                            </xsl:choose>
                        </xsl:for-each>
                    </xsl:if>
                    <xsl:if
                            test="/ead/archdesc/did/unitdate[text() != '']">
                        <dcterms:temporal>
                            <xsl:attribute name="xml:lang" select="$languageUsedForDescription"/>
                            <xsl:value-of select="/ead/archdesc/did/unitdate[text() != '']"/>
                        </dcterms:temporal>
                    </xsl:if>
                    <edm:type>
                        <xsl:value-of select="'TEXT'"/>
                    </edm:type>
                </edm:ProvidedCHO>
                <edm:WebResource>
                    <xsl:attribute name="rdf:about">
                        <xsl:choose>
                            <xsl:when test="@url != ''">
                                <xsl:value-of select="@url"/>
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:choose>
                                    <xsl:when test="$landingPage = 'ape'">
                                        <xsl:attribute name="rdf:resource"
                                                       select="concat($id_base, $eadidEncoded)"/>
                                    </xsl:when>
                                    <xsl:otherwise>
                                        <xsl:attribute name="rdf:resource"
                                                       select="normalize-space($landingPage)"/>
                                    </xsl:otherwise>
                                </xsl:choose>
                            </xsl:otherwise>
                        </xsl:choose>
                    </xsl:attribute>
                    <dc:description>
                        <xsl:attribute name="xml:lang" select="$languageUsedForDescription"/>
                        <xsl:choose>
                            <xsl:when test="/ead/archdesc/did/unittitle[text() != '']">
                                <xsl:for-each select="/ead/archdesc/did/unittitle[text() != '']">
                                    <xsl:apply-templates select="." mode="dcDescription"/>
                                    <xsl:if test="position() &lt; last()">
                                        <xsl:text> </xsl:text>
                                    </xsl:if>
                                </xsl:for-each>
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:value-of select="'Archival material'"/>
                            </xsl:otherwise>
                        </xsl:choose>
                    </dc:description>
                    <edm:rights rdf:resource="http://creativecommons.org/publicdomain/zero/1.0/"/>
                </edm:WebResource>
            </rdf:RDF>
        </xsl:result-document>
    </xsl:template>

    <xsl:template match="filedesc|profiledesc|revisiondesc"/>

    <xsl:template match="ead/archdesc">
        <xsl:apply-templates select="dsc/c">
            <xsl:with-param name="inheritedOriginations">
                <xsl:if test="./did/origination[text() != ''] or count(./did/origination/*) > 0">
                    <xsl:call-template name="creator">
                        <xsl:with-param name="originations" select="./did/origination"/>
                    </xsl:call-template>
                </xsl:if>
            </xsl:with-param>
            <xsl:with-param name="inheritedMaterialspecs">
                <xsl:if test="./did/materialspec[text() != '']">
                    <xsl:call-template name="materialspec">
                        <xsl:with-param name="materialspecs" select="./did/materialspec[text() != '']"/>
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
            <xsl:with-param name="inheritedAltformavails">
                <xsl:if test="./altformavail[text() != '']">
                    <xsl:call-template name="altformavail">
                        <xsl:with-param name="altformavails" select="./altformavail[text() != '']"/>
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
                <xsl:if test="./did/repository[descendant-or-self::text() != '']">
                    <xsl:call-template name="repository">
                        <xsl:with-param name="repository" select="./did/repository[descendant-or-self::text() != '']"/>
                    </xsl:call-template>
                </xsl:if>
            </xsl:with-param>
            <xsl:with-param name="inheritedRightsInfo">
                <xsl:choose>
                    <xsl:when test="./userestrict[@type='dao']/p[1]/extref/@xlink:href != ''">
                        <xsl:call-template name="createRights">
                            <xsl:with-param name="rights" select="./userestrict[@type='dao']"/>
                        </xsl:call-template>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:text>empty</xsl:text>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:with-param>
            <xsl:with-param name="inheritedBibref">
                <xsl:if test="./bibliography/bibref[text() != '']">
                    <xsl:for-each select="./bibliography/bibref[text() != '']">
                        <xsl:call-template name="bibref">
                            <xsl:with-param name="bibrefs" select="."/>
                        </xsl:call-template>
                    </xsl:for-each>
                </xsl:if>
            </xsl:with-param>
            <xsl:with-param name="inheritedBibliographyP">
                <xsl:if test="./bibliography/p[text() != '']">
                    <xsl:for-each select="./bibliography/p[text() != '']">
                        <xsl:call-template name="bibref">
                            <xsl:with-param name="bibrefs" select="."/>
                        </xsl:call-template>
                    </xsl:for-each>
                </xsl:if>
            </xsl:with-param>
            <xsl:with-param name="inheritedRelatedmaterial">
                <xsl:if test="./relatedmaterial[text() != '']">
                    <xsl:apply-templates select="./relatedmaterial[text() != '']"/>
                </xsl:if>
            </xsl:with-param>
        </xsl:apply-templates>
    </xsl:template>

    <xsl:template match="c">
        <xsl:param name="inheritedRepository"/>
        <xsl:param name="inheritedLanguages"/>
        <xsl:param name="inheritedRightsInfo"/>
        <xsl:param name="inheritedOriginations"/>
        <xsl:param name="inheritedMaterialspecs"/>
        <xsl:param name="inheritedControlaccesses"/>
        <xsl:param name="inheritedBibref"/>
        <xsl:param name="inheritedBibliographyP"/>
        <xsl:param name="inheritedScopecontent"/>
        <xsl:param name="inheritedRelatedmaterial"/>
        <xsl:param name="inheritedCustodhists"/>
        <xsl:param name="inheritedAltformavails"/>
        <xsl:param name="positionChain"/>

        <xsl:variable name="updatedInheritedRepository">
            <xsl:choose>
                <xsl:when test="./did/repository[descendant-or-self::text() != '']">
                    <xsl:call-template name="repository">
                        <xsl:with-param name="repository" select="./did/repository[descendant-or-self::text() != '']"/>
                    </xsl:call-template>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:copy-of select="$inheritedRepository"/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <xsl:variable name="updatedInheritedLanguages">
            <xsl:choose>
                <xsl:when test="./did/langmaterial">
                    <xsl:call-template name="language">
                        <xsl:with-param name="langmaterials" select="./did/langmaterial"/>
                    </xsl:call-template>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:copy-of select="$inheritedLanguages"/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <xsl:variable name="updatedInheritedRightsInfo">
            <xsl:choose>
                <xsl:when test="./userestrict[@type='dao']/p[1]/extref/@xlink:href != ''">
                    <xsl:call-template name="createRights">
                        <xsl:with-param name="rights" select="./userestrict[@type='dao']"/>
                    </xsl:call-template>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:copy-of select="$inheritedRightsInfo"/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <xsl:variable name="updatedInheritedOriginations">
            <xsl:choose>
                <xsl:when test="./did/origination[text() != ''] or count(./did/origination/*) > 0">
                    <xsl:call-template name="creator">
                        <xsl:with-param name="originations" select="./did/origination"/>
                    </xsl:call-template>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:copy-of select="$inheritedOriginations"/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <xsl:variable name="updatedInheritedMaterialspecs">
            <xsl:choose>
                <xsl:when test="./did/materialspec[text() != '']">
                    <xsl:call-template name="materialspec">
                        <xsl:with-param name="materialspecs" select="./did/materialspec[text() != '']"/>
                    </xsl:call-template>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:copy-of select="$inheritedMaterialspecs"/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <xsl:variable name="updatedInheritedControlaccesses">
            <xsl:choose>
                <xsl:when test="./controlaccess">
                    <xsl:call-template name="controlaccess">
                        <xsl:with-param name="controlaccesses" select="./controlaccess"/>
                    </xsl:call-template>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:copy-of select="$inheritedControlaccesses"/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <xsl:variable name="updatedInheritedBibref">
            <xsl:choose>
                <xsl:when test="./bibliography/bibref[text() != '']">
                    <xsl:for-each select="./bibliography/bibref[text() != '']">
                        <xsl:call-template name="bibref">
                            <xsl:with-param name="bibrefs" select="."/>
                        </xsl:call-template>
                    </xsl:for-each>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:copy-of select="$inheritedBibref"/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <xsl:variable name="updatedInheritedBibliographyP">
            <xsl:choose>
                <xsl:when test="./bibliography/p[text() != '']">
                    <xsl:for-each select="./bibliography/p[text() != '']">
                        <xsl:call-template name="bibref">
                            <xsl:with-param name="bibrefs" select="."/>
                        </xsl:call-template>
                    </xsl:for-each>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:copy-of select="$inheritedBibliographyP"/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <xsl:variable name="updatedInheritedScopecontent">
            <xsl:choose>
                <xsl:when test="./scopecontent[@encodinganalog='summary']">
                    <xsl:apply-templates select="./scopecontent[@encodinganalog='summary']" />
                </xsl:when>
                <xsl:otherwise>
                    <xsl:copy-of select="$inheritedScopecontent"/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <xsl:variable name="updatedInheritedRelatedmaterial">
            <xsl:choose>
                <xsl:when test="./relatedmaterial[text() != '']">
                    <xsl:apply-templates select="./relatedmaterial[text() != '']" />
                </xsl:when>
                <xsl:otherwise>
                    <xsl:copy-of select="$inheritedRelatedmaterial"/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <xsl:variable name="updatedInheritedCustodhists">
            <xsl:choose>
                <xsl:when test="./custodhist[descendant::text() != '']">
                    <xsl:call-template name="custodhist">
                        <xsl:with-param name="custodhists" select="./custodhist[descendant::text() != '']"/>
                    </xsl:call-template>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:copy-of select="$inheritedCustodhists"/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <xsl:variable name="updatedInheritedAltformavails">
            <xsl:choose>
                <xsl:when test="./altformavail[text() != '']">
                    <xsl:call-template name="altformavail">
                        <xsl:with-param name="altformavails" select="./altformavail[text() != '']"/>
                    </xsl:call-template>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:copy-of select="$inheritedAltformavails"/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <xsl:variable name="positionInDocument">
            <xsl:call-template name="number">
                <xsl:with-param name="node" select="node()"/>
            </xsl:call-template>
        </xsl:variable>

        <!-- CREATE LEVEL INFORMATION IF C OR DESCENDANT OF C HAS DAO WITH NON-EMPTY LINK-->
        <xsl:if test="did/dao[normalize-space(@xlink:href) != '' and not(@xlink:title='thumbnail') and not(@xlink:title='manifest') and not(@xlink:title='service')]">
            <xsl:call-template name="addRecord">
                <xsl:with-param name="currentnode" select="."/>
                <xsl:with-param name="inheritedRepository" select="$updatedInheritedRepository"/>
                <xsl:with-param name="inheritedLanguages" select="$updatedInheritedLanguages"/>
                <xsl:with-param name="inheritedRightsInfo" select="$updatedInheritedRightsInfo"/>
                <xsl:with-param name="inheritedOriginations" select="$updatedInheritedOriginations"/>
                <xsl:with-param name="inheritedMaterialspecs" select="$updatedInheritedMaterialspecs"/>
                <xsl:with-param name="inheritedControlaccesses" select="$updatedInheritedControlaccesses"/>
                <xsl:with-param name="inheritedBibref" select="$updatedInheritedBibref"/>
                <xsl:with-param name="inheritedBibliographyP" select="$updatedInheritedBibliographyP"/>
                <xsl:with-param name="inheritedScopecontent" select="$updatedInheritedScopecontent"/>
                <xsl:with-param name="inheritedRelatedmaterial" select="$updatedInheritedRelatedmaterial"/>
                <xsl:with-param name="inheritedCustodhists" select="$updatedInheritedCustodhists"/>
                <xsl:with-param name="inheritedAltformavails" select="$updatedInheritedAltformavails"/>
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
                <xsl:with-param name="inheritedRepository" select="$updatedInheritedRepository"/>
                <xsl:with-param name="inheritedLanguages" select="$updatedInheritedLanguages"/>
                <xsl:with-param name="inheritedRightsInfo" select="$updatedInheritedRightsInfo"/>
                <xsl:with-param name="inheritedOriginations" select="$updatedInheritedOriginations"/>
                <xsl:with-param name="inheritedMaterialspecs" select="$updatedInheritedMaterialspecs"/>
                <xsl:with-param name="inheritedControlaccesses" select="$updatedInheritedControlaccesses"/>
                <xsl:with-param name="inheritedBibref" select="$updatedInheritedBibref"/>
                <xsl:with-param name="inheritedBibliographyP" select="$updatedInheritedBibliographyP"/>
                <xsl:with-param name="inheritedScopecontent" select="$updatedInheritedScopecontent"/>
                <xsl:with-param name="inheritedRelatedmaterial" select="$updatedInheritedRelatedmaterial"/>
                <xsl:with-param name="inheritedCustodhists" select="$updatedInheritedCustodhists"/>
                <xsl:with-param name="inheritedAltformavails" select="$updatedInheritedAltformavails"/>
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
        <xsl:param name="inheritedMaterialspecs"/>
        <xsl:param name="inheritedLanguages"/>
        <xsl:param name="inheritedCustodhists"/>
        <xsl:param name="inheritedAltformavails"/>
        <xsl:param name="inheritedControlaccesses"/>
        <xsl:param name="inheritedRepository"/>
        <xsl:param name="inheritedRightsInfo"/>
        <xsl:param name="inheritedBibref"/>
        <xsl:param name="inheritedBibliographyP"/>
        <xsl:param name="inheritedRelatedmaterial"/>
        <xsl:param name="inheritedScopecontent"/>
        <xsl:param name="mainIdentifier"/>
        <xsl:param name="positionChain"/>

        <!-- VARIABLES -->
        <xsl:variable name="linkPosition" select="position()"/>
        <xsl:variable name="parentcnode" select="$currentnode/parent::node()"/>
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
                <xsl:when test="$idSource = 'unitid' and did/unitid[text() != '' and @type='call number'] and $isFirstUnitid = 'true'">
                    <xsl:value-of select="normalize-space(did/unitid[text() != '' and @type='call number'][1])"/>
                </xsl:when>
                <xsl:when test="$idSource = 'cid' and @id">
                    <xsl:value-of select="normalize-space(@id)"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="concat('position_', $mainIdentifier)"/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <xsl:variable name="identifierFilename">
            <xsl:call-template name="filenameReplace">
                <xsl:with-param name="input" select="normalize-space(encode-for-uri($identifier))"/>
            </xsl:call-template>
        </xsl:variable>
        <xsl:variable name="hasDao" select="if(did/dao[normalize-space(@xlink:href) != '' and not(@xlink:title='thumbnail') and not(@xlink:title='manifest') and not(@xlink:title='service')]) then true() else false()" />

        <!-- ACTUAL CONVERSION BEGINS HERE -->
        <xsl:result-document href="{$outputBaseDirectory}/{$eadidFilenameEncoded}/{$identifierFilename}.xml" xpath-default-namespace="urn:isbn:1-931666-22-9" >
            <rdf:RDF xmlns:adms="http://www.w3.org/ns/adms#"
                     xmlns:cc="http://creativecommons.org/ns#"
                     xmlns:crm="http://www.cidoc-crm.org/rdfs/cidoc_crm_v5.0.2_english_label.rdfs#"
                     xmlns:dc="http://purl.org/dc/elements/1.1/" xmlns:dcat="http://www.w3.org/ns/dcat#"
                     xmlns:dcterms="http://purl.org/dc/terms/" xmlns:doap="http://usefulinc.com/ns/doap#"
                     xmlns:ebucore="http://www.ebu.ch/metadata/ontologies/ebucore/ebucore#"
                     xmlns:edm="http://www.europeana.eu/schemas/edm/"
                     xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:odrl="http://www.w3.org/ns/odrl/2/"
                     xmlns:ore="http://www.openarchives.org/ore/terms/"
                     xmlns:owl="http://www.w3.org/2002/07/owl#"
                     xmlns:rdaGr2="http://rdvocab.info/ElementsGr2/"
                     xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
                     xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#"
                     xmlns:skos="http://www.w3.org/2004/02/skos/core#"
                     xmlns:svcs="http://rdfs.org/sioc/services#"
                     xmlns:wgs84_pos="http://www.w3.org/2003/01/geo/wgs84_pos#">
                <ore:Aggregation>
                    <xsl:attribute name="rdf:about"
                                   select="concat('aggregation_', normalize-space(/ead/eadheader/eadid), '_', $identifier)"/>
                    <edm:aggregatedCHO>
                        <xsl:attribute name="rdf:resource"
                                       select="concat('providedCHO_', normalize-space(/ead/eadheader/eadid), '_', $identifier)"
                        />
                    </edm:aggregatedCHO>
                    <xsl:choose>
                        <xsl:when test="$useExistingRepository = &quot;true&quot;">
                            <xsl:choose>
                                <xsl:when
                                        test="$currentnode/did/repository[descendant-or-self::text() != ''][1]">
                                    <xsl:apply-templates
                                            select="$currentnode/did/repository[descendant-or-self::text() != ''][1]"
                                    />
                                </xsl:when>
                                <xsl:when test="$inheritedRepository != ''">
                                    <xsl:call-template name="repository">
                                        <xsl:with-param name="repository"
                                                        select="$inheritedRepository"/>
                                    </xsl:call-template>
                                </xsl:when>
                                <xsl:otherwise>
                                    <edm:dataProvider>
                                        <xsl:value-of
                                                select="normalize-space($europeana_dataprovider)"/>
                                    </edm:dataProvider>
                                </xsl:otherwise>
                            </xsl:choose>
                        </xsl:when>
                        <xsl:otherwise>
                            <edm:dataProvider>
                                <xsl:value-of select="normalize-space($europeana_dataprovider)"/>
                            </edm:dataProvider>
                        </xsl:otherwise>
                    </xsl:choose>
                    <xsl:choose>
                        <xsl:when test="$hasDao">
                            <xsl:if test="count(did/dao[not(@xlink:title = 'thumbnail') and not(@xlink:title='service') and not(@xlink:title='manifest')]) > 1">
                                <xsl:apply-templates
                                        select="did/dao[not(@xlink:title = 'thumbnail') and not(@xlink:title='service') and not(@xlink:title='manifest')][position() > 1]"
                                        mode="additionalLinks"/>
                            </xsl:if>
                            <xsl:apply-templates
                                    select="did/dao[not(@xlink:title = 'thumbnail') and not(@xlink:title = 'manifest') and not(@xlink:title = 'service')][1]"
                                    mode="firstLink"/>
                            <xsl:call-template name="createIsShownAt">
                                <xsl:with-param name="landingPage" select="$landingPage"/>
                                <xsl:with-param name="idSource" select="$idSource"/>
                                <xsl:with-param name="currentnode" select="$currentnode"/>
                                <xsl:with-param name="isFirstUnitid" select="$isFirstUnitid"/>
                                <xsl:with-param name="id_base" select="$id_base"/>
                                <xsl:with-param name="eadidEncoded" select="$eadidEncoded"/>
                                <xsl:with-param name="mainIdentifier" select="$mainIdentifier"/>
                                <xsl:with-param name="identifier" select="$identifier"/>
                            </xsl:call-template>

                            <edm:provider>
                                <xsl:value-of select="$europeana_provider"/>
                            </edm:provider>
                            <xsl:if test="$dc_rights and normalize-space($dc_rights) != ''">
                                <dc:rights>
                                    <xsl:value-of select="$dc_rights"/>
                                </dc:rights>
                            </xsl:if>
                            <xsl:choose>
                                <xsl:when test="$useExistingRightsInfo = 'true'">
                                    <xsl:choose>
                                        <xsl:when
                                                test="$currentnode/userestrict[@type = 'dao']/p[1]/extref/@xlink:href != ''">
                                            <xsl:call-template name="createRights">
                                                <xsl:with-param name="rights"
                                                                select="$currentnode/userestrict[@type = 'dao']"/>
                                            </xsl:call-template>
                                        </xsl:when>
                                        <xsl:when test="$inheritedRightsInfo != 'empty'">
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
                                        <xsl:when test="$inheritedRightsInfo != 'empty'">
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
                            <xsl:call-template name="createIsShownAt">
                                <xsl:with-param name="landingPage" select="$landingPage"/>
                                <xsl:with-param name="idSource" select="$idSource"/>
                                <xsl:with-param name="currentnode" select="$currentnode"/>
                                <xsl:with-param name="isFirstUnitid" select="$isFirstUnitid"/>
                                <xsl:with-param name="id_base" select="$id_base"/>
                                <xsl:with-param name="eadidEncoded" select="$eadidEncoded"/>
                                <xsl:with-param name="mainIdentifier" select="$mainIdentifier"/>
                                <xsl:with-param name="identifier" select="$identifier"/>
                            </xsl:call-template>
                            <edm:object>
                                <xsl:attribute name="rdf:resource"
                                               select="concat('http://', $host, '/Portal-theme/images/ape/icons/dao_types/europeana/text.png')"
                                />
                            </edm:object>
                            <edm:provider>
                                <xsl:value-of select="$europeana_provider"/>
                            </edm:provider>
                            <edm:rights
                                    rdf:resource="http://creativecommons.org/publicdomain/zero/1.0/"/>
                        </xsl:otherwise>
                    </xsl:choose>
                </ore:Aggregation>
                <edm:ProvidedCHO>
                    <xsl:attribute name="rdf:about"
                                   select="concat('providedCHO_', normalize-space(/ead/eadheader/eadid), '_', $identifier)"/>
                    <xsl:if test="$idSource = 'unitid' and $currentnode/did/unitid[text() != '']">
                        <dc:identifier>
                            <xsl:for-each select="$currentnode/did/unitid[text() != '']">
                                <xsl:apply-templates select="."/>
                                <xsl:if test="position() &lt; last()">
                                    <xsl:text> </xsl:text>
                                </xsl:if>
                            </xsl:for-each>
                        </dc:identifier>
                    </xsl:if>
                    <xsl:if test="$idSource = 'cid' and $currentnode/@id">
                        <dc:identifier>
                            <xsl:value-of select="$currentnode/@id"/>
                        </dc:identifier>
                    </xsl:if>
                    <xsl:choose>
                        <xsl:when
                                test="$currentnode/did/origination[text() != ''] or count($currentnode/did/origination/*) > 0">
                            <xsl:call-template name="creator">
                                <xsl:with-param name="originations"
                                                select="$currentnode/did/origination"/>
                            </xsl:call-template>
                        </xsl:when>
                        <xsl:when
                                test="fn:string-length($inheritedOriginations) > 0">
                            <xsl:copy-of select="$inheritedOriginations"/>
                        </xsl:when>
                    </xsl:choose>
                    <xsl:if test="$currentnode/did/unitdate">
                        <xsl:apply-templates select="$currentnode/did/unitdate"/>
                    </xsl:if>
                    <xsl:choose>
                        <xsl:when test="$currentnode/scopecontent[@encodinganalog = 'summary']">
                            <xsl:apply-templates
                                    select="$currentnode/scopecontent[@encodinganalog = 'summary']"/>
                        </xsl:when>
                        <xsl:when
                                test="fn:string-length($inheritedScopecontent) > 0">
                            <xsl:copy-of select="$inheritedScopecontent"/>
                        </xsl:when>
                    </xsl:choose>
                    <xsl:choose>
                        <xsl:when test="$currentnode/did/materialspec[text() != '']">
                            <xsl:call-template name="materialspec">
                                <xsl:with-param name="materialspecs" select="$currentnode/did/materialspec[text() != '']"/>
                            </xsl:call-template>
                        </xsl:when>
                        <xsl:when test="fn:string-length($inheritedMaterialspecs) > 0">
                            <xsl:copy-of select="$inheritedMaterialspecs"/>
                        </xsl:when>
                    </xsl:choose>
                    <xsl:choose>
                        <xsl:when test="$currentnode/relatedmaterial[text() != '']">
                            <xsl:apply-templates select="$currentnode/relatedmaterial[text() != '']"
                            />
                        </xsl:when>
                        <xsl:when test="fn:string-length($inheritedRelatedmaterial) > 0">
                            <xsl:copy-of select="$inheritedRelatedmaterial"/>
                        </xsl:when>
                    </xsl:choose>
                    <dc:type>
                        <xsl:attribute name="rdf:resource">
                            <xsl:text>http://vocab.getty.edu/aat/300379505</xsl:text>
                        </xsl:attribute>
                    </dc:type>
                    <xsl:if test="$currentnode/did/physdesc/genreform[text() != '']">
                        <dc:type>
                            <xsl:attribute name="xml:lang" select="$languageUsedForDescription"/>
                            <xsl:value-of select="$currentnode/did/physdesc/genreform[text() != '']"/>
                        </dc:type>
                    </xsl:if>

                    <xsl:if
                            test="$currentnode/did/physdesc/physfacet[text() != '']">
                        <dc:format>
                            <xsl:attribute name="xml:lang" select="$languageUsedForDescription"/>
                            <xsl:value-of select="$currentnode/did/physdesc/physfacet[text() != '']" />
                        </dc:format>
                    </xsl:if>
                    <xsl:if
                            test="$currentnode/did/physdesc/extent[text() != '']">
                        <dcterms:extent>
                            <xsl:value-of select="$currentnode/did/physdesc/extent[text() != '']"/>
                        </dcterms:extent>
                    </xsl:if>
                    <xsl:if
                            test="$currentnode/did/physdesc/dimensions[text() != '']">
                        <dcterms:extent>
                            <xsl:value-of
                                    select="$currentnode/did/physdesc/dimensions[text() != '']"/>
                        </dcterms:extent>
                    </xsl:if>
                    <xsl:choose>
                        <xsl:when test="$currentnode/otherfindaid">
                            <xsl:apply-templates select="$currentnode/otherfindaid"/>
                        </xsl:when>
                        <xsl:when test="$parentcnode/otherfindaid">
                            <xsl:apply-templates select="$parentcnode/otherfindaid"/>
                        </xsl:when>
                    </xsl:choose>
                    <xsl:choose>
                        <xsl:when
                                test="$currentnode/bibliography/bibref[text() != '']">
                            <xsl:for-each select="$currentnode/bibliography/bibref[text() != '']">
                                <xsl:call-template name="bibref">
                                    <xsl:with-param name="bibrefs" select="."/>
                                </xsl:call-template>
                            </xsl:for-each>
                        </xsl:when>
                        <xsl:when test="fn:string-length($inheritedBibref) > 0">
                            <xsl:copy-of select="$inheritedBibref"/>
                        </xsl:when>
                    </xsl:choose>
                    <xsl:choose>
                        <xsl:when
                                test="$currentnode/bibliography/p[text() != '']">
                            <xsl:for-each select="$currentnode/bibliography/p[text() != '']">
                                <xsl:call-template name="bibref">
                                    <xsl:with-param name="bibrefs" select="."/>
                                </xsl:call-template>
                            </xsl:for-each>
                        </xsl:when>
                        <xsl:when test="fn:string-length($inheritedBibliographyP) > 0">
                            <xsl:copy-of select="$inheritedBibliographyP"/>
                        </xsl:when>
                    </xsl:choose>

                    <!-- custodhist -->
                    <xsl:choose>
                        <xsl:when test="$currentnode/custodhist[descendant::text() != '']">
                            <xsl:call-template name="custodhist">
                                <xsl:with-param name="custodhists"
                                                select="$currentnode/custodhist[descendant::text() != '']"/>
                            </xsl:call-template>
                        </xsl:when>
                        <xsl:when test="$parentcnode/custodhist[descendant::text() != '']">
                            <xsl:call-template name="custodhistOnlyOne">
                                <xsl:with-param name="custodhists"
                                                select="$parentcnode/custodhist[descendant::text() != '']"/>
                                <xsl:with-param name="parentnode" select="$parentcnode"/>
                            </xsl:call-template>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:if test="fn:string-length($inheritedCustodhists) > 0">
                                <xsl:copy-of select="$inheritedCustodhists"/>
                            </xsl:if>
                        </xsl:otherwise>
                    </xsl:choose>

                    <!-- relatedmaterial -->
                    <xsl:choose>
                        <xsl:when test="$currentnode/relatedmaterial[descendant::text() != '']">
                            <xsl:call-template name="relatedmaterial">
                                <xsl:with-param name="relatedmaterials"
                                                select="$currentnode/relatedmaterial[descendant::text() != '']"/>
                            </xsl:call-template>
                        </xsl:when>
                        <xsl:when test="$parentcnode/relatedmaterial[descendant::text() != '']">
                            <xsl:call-template name="relatedmaterialOnlyOne">
                                <xsl:with-param name="relatedmaterials"
                                                select="$parentcnode/relatedmaterial[descendant::text() != '']"/>
                                <xsl:with-param name="parentnode" select="$parentcnode"/>
                            </xsl:call-template>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:if test="fn:string-length($inheritedRelatedmaterial) > 0">
                                <xsl:copy-of select="$inheritedRelatedmaterial"/>
                            </xsl:if>
                        </xsl:otherwise>
                    </xsl:choose>

                    <xsl:choose>
                        <xsl:when test="$currentnode/altformavail[text() != '']">
                            <xsl:call-template name="altformavail">
                                <xsl:with-param name="altformavails"
                                                select="$currentnode/altformavail[text() != '']"/>
                            </xsl:call-template>
                        </xsl:when>
                        <xsl:when test="fn:string-length($inheritedAltformavails) > 0">
                            <xsl:copy-of select="$inheritedAltformavails"/>
                        </xsl:when>
                    </xsl:choose>

                    <xsl:choose>
                        <xsl:when test="$currentnode/controlaccess">
                            <xsl:call-template name="controlaccess">
                                <xsl:with-param name="controlaccesses"
                                                select="$currentnode/controlaccess"/>
                            </xsl:call-template>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:if test="fn:string-length($inheritedControlaccesses) > 0">
                                <xsl:copy-of select="$inheritedControlaccesses"/>
                            </xsl:if>
                        </xsl:otherwise>
                    </xsl:choose>

                    <xsl:choose>
                        <xsl:when test="$inheritUnittitle = true() and $currentnode/did/unittitle != '' and $parentcnode/did/unittitle != '' and not($currentnode/c) and $hasDao">
                            <dc:title>
                                <xsl:attribute name="xml:lang" select="$languageUsedForDescription"/>
                                <xsl:value-of select="$parentcnode/did/unittitle[1]"/> >>
                                <xsl:value-of select="$currentnode/did/unittitle"/>
                            </dc:title>
                        </xsl:when>
                        <xsl:when test="$inheritUnittitle = true() and $currentnode/did/unittitle != '' and $parentcnode/did/unittitle = '' and not($currentnode/c) and $hasDao">
                            <dc:title>
                                <xsl:attribute name="xml:lang" select="$languageUsedForDescription"/>
                                <xsl:value-of select="$currentnode/did/unittitle"/>
                            </dc:title>
                        </xsl:when>
                        <xsl:when test="$inheritUnittitle = true() and $currentnode/did/unittitle = '' and $parentcnode/did/unittitle != '' and not($currentnode/c) and $hasDao">
                            <dc:title>
                                <xsl:attribute name="xml:lang" select="$languageUsedForDescription"/>
                                <xsl:value-of select="$parentcnode/did/unittitle[1]"/>"/>
                            </dc:title>
                        </xsl:when>
                        <xsl:when test="$currentnode/did/unittitle != ''">
                            <dc:title>
                                <xsl:attribute name="xml:lang" select="$languageUsedForDescription"/>
                                <xsl:value-of select="$currentnode/did/unittitle"/>
                            </dc:title>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:if test="not($currentnode/scopecontent[@encodinganalog = 'summary'] != '') and $inheritedScopecontent = ''">
                                <dc:title>
                                    <xsl:attribute name="xml:lang" select="$languageUsedForDescription"/>
                                    <xsl:text>No title</xsl:text>
                                </dc:title>
                            </xsl:if>
                        </xsl:otherwise>
                    </xsl:choose>

                    <xsl:choose>
                        <xsl:when test="$useExistingLanguageMaterial = &quot;true&quot;">
                            <xsl:choose>
                                <xsl:when test="$currentnode/did/langmaterial">
                                    <xsl:call-template name="language">
                                        <xsl:with-param name="langmaterials"
                                                        select="$currentnode/did/langmaterial"/>
                                    </xsl:call-template>
                                </xsl:when>
                                <xsl:when test="$parentcnode/did/langmaterial">
                                    <xsl:call-template name="language">
                                        <xsl:with-param name="langmaterials"
                                                        select="$parentcnode/did/langmaterial"/>
                                    </xsl:call-template>
                                </xsl:when>
                                <xsl:when test="fn:string-length($inheritedLanguages) > 0">
                                    <xsl:copy-of select="$inheritedLanguages"/>
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:choose>
                                        <xsl:when test="fn:string-length($inheritedLanguages) > 0">
                                            <xsl:for-each
                                                    select="tokenize($inheritedLanguages, ' ')">
                                                <dc:language>
                                                    <xsl:value-of select="."/>
                                                </dc:language>
                                            </xsl:for-each>
                                        </xsl:when>
                                        <xsl:otherwise>
                                            <xsl:for-each select="tokenize($languageMaterial, ' ')">
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
                                <xsl:when test="$parentcnode/did/langmaterial">
                                    <xsl:call-template name="language">
                                        <xsl:with-param name="langmaterials"
                                                        select="$parentcnode/did/langmaterial"/>
                                    </xsl:call-template>
                                </xsl:when>
                                <xsl:when test="fn:string-length($inheritedLanguages) > 0">
                                    <xsl:copy-of select="$inheritedLanguages"/>
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:choose>
                                        <xsl:when test="fn:string-length($languageMaterial) > 0">
                                            <xsl:for-each select="tokenize($languageMaterial, ' ')">
                                                <dc:language>
                                                    <xsl:value-of select="."/>
                                                </dc:language>
                                            </xsl:for-each>
                                        </xsl:when>
                                        <xsl:otherwise>
                                            <xsl:for-each select="tokenize($languageMaterial, ' ')">
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
                            <xsl:with-param name="positionInDocument"
                                            select="$positionOfParentInDocument"/>
                            <xsl:with-param name="currentCNode" select="$parentcnode"/>
                        </xsl:call-template>
                    </xsl:variable>

                    <dcterms:isPartOf>
                        <xsl:attribute name="rdf:resource"
                                       select="concat('providedCHO_', normalize-space(/ead/eadheader/eadid))"/>
                    </dcterms:isPartOf>

                    <xsl:if test="$currentnode/preceding-sibling::c">
                        <xsl:variable name="positionOfSiblingInDocument">
                            <xsl:call-template name="number">
                                <xsl:with-param name="node"
                                                select="$currentnode/preceding-sibling::c[1]"/>
                            </xsl:call-template>
                        </xsl:variable>
                        <xsl:variable name="isSiblingFirstUnitid">
                            <xsl:call-template name="detectFirstUnitid">
                                <xsl:with-param name="positionInDocument"
                                                select="$positionOfSiblingInDocument"/>
                                <xsl:with-param name="currentCNode"
                                                select="$currentnode/preceding-sibling::c[1]"/>
                            </xsl:call-template>
                        </xsl:variable>
                        <xsl:choose>
                            <xsl:when
                                    test="$idSource = 'unitid' and $currentnode/preceding-sibling::*[descendant::did/dao[normalize-space(@xlink:href) != '']][1]/did/unitid[text() != '' and @type = 'call number'] and not(key('unitids', $currentnode/preceding-sibling::*[descendant::did/dao[normalize-space(@xlink:href) != '']][1]/did/unitid[text() != '' and @type = 'call number'])[2])">
                                <edm:isNextInSequence>
                                    <xsl:attribute name="rdf:resource"
                                                   select="concat('providedCHO_', normalize-space(/ead/eadheader/eadid), '_', normalize-space($currentnode/preceding-sibling::*[descendant::did/dao[normalize-space(@xlink:href) != '']][1]/did/unitid[text() != '' and @type = 'call number'][1]))"
                                    />
                                </edm:isNextInSequence>
                            </xsl:when>
                            <xsl:when
                                    test="$idSource = 'cid' and $currentnode/preceding-sibling::*[descendant::did/dao[normalize-space(@xlink:href) != '']][1]/@id">
                                <edm:isNextInSequence>
                                    <xsl:attribute name="rdf:resource"
                                                   select="concat('providedCHO_', normalize-space(/ead/eadheader/eadid), '_', normalize-space($currentnode/preceding-sibling::*[descendant::did/dao[normalize-space(@xlink:href) != '']][1]/@id))"
                                    />
                                </edm:isNextInSequence>
                            </xsl:when>
                            <xsl:when
                                    test="$currentnode/preceding-sibling::*[descendant::did/dao[normalize-space(@xlink:href) != '']][1]">
                                <edm:isNextInSequence>
                                    <xsl:attribute name="rdf:resource">
                                        <xsl:call-template name="number">
                                            <xsl:with-param name="prefix">
                                                <xsl:choose>
                                                    <xsl:when test="$positionChain">
                                                        <xsl:value-of
                                                                select="concat('providedCHO_position_', normalize-space(/ead/eadheader/eadid), '_', $positionChain, '-')"
                                                        />
                                                    </xsl:when>
                                                    <xsl:otherwise>
                                                        <xsl:value-of
                                                                select="concat('providedCHO_position_', normalize-space(/ead/eadheader/eadid), '_')"
                                                        />
                                                    </xsl:otherwise>
                                                </xsl:choose>
                                            </xsl:with-param>
                                            <xsl:with-param name="node"
                                                            select="$currentnode/preceding-sibling::*[descendant::did/dao[normalize-space(@xlink:href) != '']][1]"
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
                                <xsl:when test="$useExistingDaoRole = 'true'">
                                    <xsl:choose>
                                        <xsl:when
                                                test="did/dao[not(@xlink:title = 'thumbnail') and not(@xlink:title='manifest') and not(@xlink:title='service')][1]/@xlink:role">
                                            <edm:type>
                                                <xsl:call-template name="convertToEdmType">
                                                    <xsl:with-param name="role"
                                                                    select="did/dao[not(@xlink:title = 'thumbnail') and not(@xlink:title='manifest') and not(@xlink:title='service')][1]/@xlink:role"
                                                    />
                                                </xsl:call-template>
                                            </edm:type>
                                        </xsl:when>
                                        <xsl:otherwise>
                                            <xsl:if test="fn:string-length($europeana_type) > 0">
                                                <edm:type>
                                                    <xsl:call-template name="convertToEdmType">
                                                        <xsl:with-param name="role"
                                                                        select="$europeana_type"/>
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
                                                    <xsl:with-param name="role"
                                                                    select="$europeana_type"/>
                                                </xsl:call-template>
                                            </edm:type>
                                        </xsl:when>
                                        <xsl:otherwise>
                                            <xsl:if
                                                    test="did/dao[not(@xlink:title = 'thumbnail') and not(@xlink:title='manifest') and not(@xlink:title='service')][1]/@xlink:role">
                                                <edm:type>
                                                    <xsl:call-template name="convertToEdmType">
                                                        <xsl:with-param name="role"
                                                                        select="did/dao[not(@xlink:title = 'thumbnail') and not(@xlink:title='manifest') and not(@xlink:title='service')][1]/@xlink:role"
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
                        <xsl:apply-templates select="did/dao[not(@xlink:title='thumbnail') and not(@xlink:title='manifest') and not(@xlink:title='service')][1][normalize-space(@xlink:href) != '']"
                                             mode="webResource">
                            <xsl:with-param name="manifestDao" select="did/dao[@xlink:title='manifest'][1]"/>
                            <xsl:with-param name="serviceDao" select="did/dao[@xlink:title='service'][1]"/>
                            <xsl:with-param name="inheritedRightsInfo" select="$inheritedRightsInfo"
                            />
                        </xsl:apply-templates>
                    </xsl:when>
                    <xsl:otherwise>
                        <edm:WebResource>
                            <xsl:attribute name="rdf:about">
                                <xsl:choose>
                                    <xsl:when test="@url != ''">
                                        <xsl:value-of select="@url"/>
                                    </xsl:when>
                                    <xsl:otherwise>
                                        <xsl:choose>
                                            <xsl:when test="$landingPage = 'ape'">
                                                <xsl:choose>
                                                    <xsl:when
                                                            test="$idSource = 'unitid' and did/unitid[text() != '' and @type = 'call number'][1] and $isFirstUnitid = 'true'">
                                                        <xsl:variable name="unitidEncoded">
                                                            <xsl:call-template name="simpleReplace">
                                                                <xsl:with-param name="input"
                                                                                select="normalize-space(did/unitid[text() != '' and @type = 'call number'][1])"
                                                                />
                                                            </xsl:call-template>
                                                        </xsl:variable>
                                                        <xsl:value-of
                                                                select="concat($id_base, $eadidEncoded, '/unitid/', $unitidEncoded)"
                                                        />
                                                    </xsl:when>
                                                    <xsl:when test="$idSource = 'cid' and @id">
                                                        <xsl:value-of
                                                                select="concat($id_base, $eadidEncoded, '/cid/', @id)"
                                                        />
                                                    </xsl:when>
                                                    <xsl:when test="$mainIdentifier">
                                                        <xsl:value-of
                                                                select="concat($id_base, $eadidEncoded, '/position/', $mainIdentifier)"
                                                        />
                                                    </xsl:when>
                                                    <xsl:otherwise>
                                                        <xsl:call-template name="number">
                                                            <xsl:with-param name="prefix"
                                                                            select="concat($id_base, normalize-space($eadidEncoded), '/position/')"/>
                                                            <xsl:with-param name="node" select="."/>
                                                        </xsl:call-template>
                                                    </xsl:otherwise>
                                                </xsl:choose>
                                            </xsl:when>
                                            <xsl:otherwise>
                                                <xsl:value-of
                                                        select="concat($landingPage, '/', $identifier)"/>
                                            </xsl:otherwise>
                                        </xsl:choose>
                                    </xsl:otherwise>
                                </xsl:choose>
                            </xsl:attribute>
                            <dc:description>
                                <xsl:attribute name="xml:lang" select="$languageUsedForDescription"/>
                                <xsl:choose>
                                    <xsl:when test="did/unittitle != ''">
                                        <xsl:apply-templates select="did/unittitle"
                                                             mode="dcDescription"/>
                                    </xsl:when>
                                    <xsl:otherwise>
                                        <xsl:value-of select="'Archival material'"/>
                                    </xsl:otherwise>
                                </xsl:choose>
                            </dc:description>
                            <edm:rights
                                    rdf:resource="http://creativecommons.org/publicdomain/zero/1.0/"/>
                        </edm:WebResource>
                    </xsl:otherwise>
                </xsl:choose>
                <!--</xsl:for-each>-->
            </rdf:RDF>
        </xsl:result-document>
    </xsl:template>
    <xsl:template name="createIsShownAt">
        <xsl:param name="landingPage"/>
        <xsl:param name="idSource"/>
        <xsl:param name="currentnode"/>
        <xsl:param name="isFirstUnitid"/>
        <xsl:param name="id_base"/>
        <xsl:param name="eadidEncoded"/>
        <xsl:param name="mainIdentifier"/>
        <xsl:param name="identifier"/>
        <edm:isShownAt>
            <xsl:choose>
                <xsl:when test="@url and @url != ''">
                    <xsl:attribute name="rdf:resource" select="@url"/>
                </xsl:when>
                <xsl:when test="$currentnode/did/unitid[@type = 'call number']/extptr[@xlink:href != '']/@xlink:href and $currentnode/did/unitid[@type = 'call number']/extptr[@xlink:href != '']/@xlink:href != ''">
                    <xsl:attribute name="rdf:resource" select="$currentnode/did/unitid[@type = 'call number']/extptr[@xlink:href != '']/@xlink:href"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:attribute name="rdf:resource">
                        <xsl:choose>
                            <xsl:when test="$landingPage = 'ape'">
                                <xsl:choose>
                                    <xsl:when
                                            test="$idSource = 'unitid' and $currentnode/did/unitid[text() != '' and @type = 'call number'][1] and $isFirstUnitid = 'true'">
                                        <xsl:variable name="unitidEncoded">
                                            <xsl:call-template name="simpleReplace">
                                                <xsl:with-param name="input"
                                                                select="normalize-space($currentnode/did/unitid[text() != '' and @type = 'call number'][1])"
                                                />
                                            </xsl:call-template>
                                        </xsl:variable>
                                        <xsl:value-of
                                                select="concat($id_base, $eadidEncoded, '/unitid/', $unitidEncoded)"
                                        />
                                    </xsl:when>
                                    <xsl:when test="$idSource = 'cid' and @id">
                                        <xsl:value-of
                                                select="concat($id_base, $eadidEncoded, '/cid/', @id)"/>
                                    </xsl:when>
                                    <xsl:when test="$mainIdentifier">
                                        <xsl:value-of
                                                select="concat($id_base, $eadidEncoded, '/position/', $mainIdentifier)"
                                        />
                                    </xsl:when>
                                    <xsl:otherwise>
                                        <xsl:call-template name="number">
                                            <xsl:with-param name="prefix"
                                                            select="concat($id_base, normalize-space($eadidEncoded), '/position/')"/>
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
        <xsl:for-each select="$controlaccesses/corpname | $controlaccesses/persname | $controlaccesses/famname | $controlaccesses/name">
            <xsl:if test="text() != ''">
                <dc:coverage>
                    <xsl:attribute name="xml:lang" select="$languageUsedForDescription"/>
                    <xsl:value-of select="."/>
                </dc:coverage>
            </xsl:if>
        </xsl:for-each>
        <xsl:for-each select="$controlaccesses/geogname">
            <xsl:if test="text() != ''">
                <dcterms:spatial>
                    <xsl:attribute name="xml:lang" select="$languageUsedForDescription"/>
                    <xsl:value-of select="."/>
                </dcterms:spatial>
            </xsl:if>
        </xsl:for-each>
        <xsl:for-each select="$controlaccesses/function | $controlaccesses/occupation | $controlaccesses/subject">
            <xsl:if test="text() != ''">
                <dc:subject>
                    <xsl:attribute name="xml:lang" select="$languageUsedForDescription"/>
                    <xsl:value-of select="."/>
                </dc:subject>
            </xsl:if>
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
                    <xsl:when test="$useExistingRightsInfo='true'">
                        <xsl:choose>
                            <xsl:when test="$rights[1]/p[1]/extref/@xlink:href">
                                <xsl:variable name="currentRightsInfo">
                                    <xsl:choose>
                                        <xsl:when test="not(ends-with($rights[1]/p[1]/extref/@xlink:href, '/'))">
                                            <xsl:value-of select="concat($rights[1]/p[1]/extref/@xlink:href, '/')"/>
                                        </xsl:when>
                                        <xsl:otherwise>
                                            <xsl:value-of select="$rights[1]/p[1]/extref/@xlink:href"/>
                                        </xsl:otherwise>
                                    </xsl:choose>
                                </xsl:variable>
                                <xsl:value-of select="$currentRightsInfo"/>
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
            <xsl:variable name="text">
                <xsl:value-of select="fn:replace(normalize-space(string-join(., ' ')), '[\n\t\r]', '')"/>
            </xsl:variable>
            <xsl:if test="fn:string-length($text) > 0">
                <xsl:element name="dc:creator">
                    <xsl:value-of select="$text"/>
                </xsl:element>
            </xsl:if>
        </xsl:for-each>
    </xsl:template>
    <xsl:template name="custodhist">
        <xsl:param name="custodhists"/>
        <xsl:for-each select="$custodhists">
            <xsl:variable name="content">
                <xsl:apply-templates select="head" />
                <xsl:for-each select="p | list/item | table">
                    <xsl:apply-templates />
                    <xsl:if test="position() &lt; last()"><xsl:text> </xsl:text></xsl:if>
                </xsl:for-each>
            </xsl:variable>
            <dcterms:provenance>
                <xsl:attribute name="xml:lang" select="$languageUsedForDescription"/>
                <xsl:value-of select="fn:replace(normalize-space($content), '[\n\t\r]', ' ')"/>
            </dcterms:provenance>
        </xsl:for-each>
    </xsl:template>
    <xsl:template name="custodhistOnlyOne">
        <xsl:param name="custodhists"/>
        <xsl:param name="parentnode"/>
        <xsl:variable name="needsLinkToEadUrl">
            <xsl:choose>
                <xsl:when test="exists($custodhists/head[2]) or exists($custodhists/p[2])">
                    <xsl:value-of select="true()"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="false()"/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <xsl:for-each select="$custodhists">
            <xsl:variable name="content">
                <xsl:apply-templates select="head[1] | p[1]"/>
            </xsl:variable>
            <dcterms:provenance>
                <xsl:attribute name="xml:lang" select="$languageUsedForDescription"/>
                <xsl:value-of select="fn:replace(normalize-space($content), '[\n\t\r]', ' ')"/>
            </dcterms:provenance>
        </xsl:for-each>
        <xsl:if test="$needsLinkToEadUrl = true()">
            <dcterms:provenance>
                <xsl:attribute name="xml:lang" select="$languageUsedForDescription"/>
                <xsl:attribute name="rdf:resource">
                    <xsl:choose>
                        <xsl:when test="/ead/eadheader/eadid/@url">
                            <xsl:value-of select="/ead/eadheader/eadid/@url"/>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:value-of
                                    select="concat($id_base, normalize-space($eadidEncoded))"/>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:attribute>
            </dcterms:provenance>
        </xsl:if>
    </xsl:template>
    <xsl:template name="detectFirstUnitid">
        <xsl:param name="positionInDocument"/>
        <xsl:param name="currentCNode"/>
        <xsl:choose>
            <xsl:when test="key('unitids', $currentCNode/did/unitid[text() != '' and @type='call number'])[2]">
                <xsl:variable name="firstElement">
                    <xsl:choose>
                        <xsl:when test="local-name(key('unitids', $currentCNode/did/unitid[text() != '' and @type='call number'])[1]/../..) = 'archdesc'">
                            <xsl:value-of select="'archdesc'"/>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:call-template name="number">
                                <xsl:with-param name="node"
                                                select="key('unitids', $currentCNode/did/unitid[text() != '' and @type='call number'])[1]/../.."/>
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
    <xsl:template name="language">
        <xsl:param name="langmaterials"/>
        <xsl:for-each select="$langmaterials/language/@langcode">
            <xsl:variable name="languageMaterialWithoutSpaces">
                <xsl:value-of select="fn:replace(normalize-space(.), '[\n\t\r]', '')"/>
            </xsl:variable>
            <dc:language>
                <xsl:choose>
                    <xsl:when test="fn:string-length($languageMaterialWithoutSpaces) > 0">
                        <xsl:value-of select="$languageMaterialWithoutSpaces"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:for-each select="tokenize($languageMaterial,' ')">
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
    <xsl:template name="positionForHasPart">
        <xsl:param name="node"/>
        <xsl:param name="postfix"/>
        <xsl:variable name="number">
            <xsl:number count="c" level="single" select="$node[1]"/>
        </xsl:variable>
        <xsl:choose>
            <xsl:when test="$node/parent::c">
                <xsl:call-template name="positionForHasPart">
                    <xsl:with-param name="node" select="$node/parent::c"/>
                    <xsl:with-param name="postfix" select="concat('_c',$number - 1, $postfix)"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:when test="$node/parent::dsc">
                <xsl:value-of select="concat('providedCHO_', normalize-space(/ead/eadheader/eadid), '_position_c', $number - 1, $postfix)"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:text>Error while calculating position</xsl:text>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    <xsl:template name="repository">
        <xsl:param name="repository"/>
        <edm:dataProvider>
            <xsl:variable name="content">
                <xsl:apply-templates select="$repository[descendant-or-self::text() != ''][1]"/>
            </xsl:variable>
            <xsl:value-of select="fn:replace(normalize-space($content), '[\n\t\r]', '')"/>
        </edm:dataProvider>
    </xsl:template>
    <xsl:template name="relatedmaterial">
        <xsl:param name="relatedmaterials"/>
        <xsl:for-each select="$relatedmaterials">
            <xsl:variable name="content">
                <xsl:apply-templates select="head" />
                <xsl:for-each select="p | list/item | table">
                    <xsl:apply-templates />
                    <xsl:if test="position() &lt; last()"><xsl:text> </xsl:text></xsl:if>
                </xsl:for-each>
            </xsl:variable>
            <dc:relation>
                <xsl:attribute name="xml:lang" select="$languageUsedForDescription"/>
                <xsl:value-of select="fn:replace(normalize-space($content), '[\n\t\r]', ' ')"/>
            </dc:relation>
        </xsl:for-each>
    </xsl:template>
    <xsl:template name="relatedmaterialOnlyOne">
        <xsl:param name="relatedmaterials"/>
        <xsl:param name="parentnode"/>
        <xsl:variable name="needsLinkToEadUrl">
            <xsl:choose>
                <xsl:when test="exists($relatedmaterials/head[2]) or exists($relatedmaterials/p[2])">
                    <xsl:value-of select="true()"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="false()"/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <xsl:for-each select="$relatedmaterials">
            <xsl:variable name="content">
                <xsl:apply-templates select="head[1] | p[1]"/>
            </xsl:variable>
            <dc:relation>
                <xsl:attribute name="xml:lang" select="$languageUsedForDescription"/>
                <xsl:value-of select="fn:replace(normalize-space($content), '[\n\t\r]', ' ')"/>
            </dc:relation>
        </xsl:for-each>
        <xsl:if test="$needsLinkToEadUrl = true()">
            <dc:relation>
                <xsl:attribute name="xml:lang" select="$languageUsedForDescription"/>
                <xsl:attribute name="rdf:resource">
                    <xsl:choose>
                        <xsl:when test="/ead/eadheader/eadid/@url">
                            <xsl:value-of select="/ead/eadheader/eadid/@url"/>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:value-of
                                    select="concat($id_base, normalize-space($eadidEncoded))"/>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:attribute>
            </dc:relation>
        </xsl:if>
    </xsl:template>
    <xsl:template name="simpleReplace">
        <xsl:param name="input"/>
        <xsl:choose>
            <xsl:when test="contains($input, '%2B') or contains($input, '%2F') or contains($input, '%3A') or contains($input, '%2A') or contains($input, '%26') or contains($input, '%2C') or contains($input, '&lt;') or contains($input, '&gt;')or contains($input, '%27')
                or contains($input, '~') or contains($input, '%5B') or contains($input, '%5D') or contains($input, '%20') or contains($input, '%5C') or contains($input, '%40') or contains($input, '%22') or contains($input, '%24')
                or contains($input, '%3D') or contains($input, '%23') or contains($input, '%5E') or contains($input, '%28') or contains($input, '%29') or contains($input, '%21') or contains($input, '%3B') or contains($input, '%25')">
                <xsl:variable name="replaceResult1" select="replace(replace(replace(replace(replace(replace(replace(replace(replace($input, '%2B', '_PLUS_'), '%2F', '_SLASH_'), '%3A', '_COLON_'), '%2A', '_ASTERISK_'), '%26', '_AMP_'), '%2C', '_COMMA_'), '&lt;', '_LT_'), '&gt;', '_RT_'), '%27', '_SQUOTE_')"/>
                <xsl:variable name="replaceResult2" select="replace(replace(replace(replace(replace(replace(replace(replace($replaceResult1, '~', '_TILDE_'), '%5B', '_LSQBRKT_'), '%5D', '_RSQBRKT_'), '%20', '+'), '%5C', '_BSLASH_'), '%40', '_ATCHAR_'), '%22', '_QUOTE_'), '%24', '_DOLLAR_')"/>
                <xsl:variable name="replaceResult3" select="replace(replace(replace(replace(replace(replace(replace(replace($replaceResult2, '%3D', '_COMP_'), '%23', '_HASH_'), '%5E', '_CFLEX_'), '%28', '_LRDBRKT_'), '%29', '_RRDBRKT_'), '%21', '_EXCLMARK_'), '%3B', '_SEMICOLON_'), '%25', '_PERCENT_')"/>
                <xsl:value-of select="$replaceResult3"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="$input"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    <xsl:template name="filenameReplace">
        <xsl:param name="input"/>
        <xsl:choose>
            <xsl:when test="contains($input, '%')">
                <xsl:variable name="replaceResult1" select="replace($input, '%20', '+')"></xsl:variable>
                <xsl:variable name="replaceResult2" select="replace($replaceResult1, '%', '%25')"/>
                <xsl:value-of select="$replaceResult2"/>
            </xsl:when><xsl:otherwise>
            <xsl:value-of select="$input"/>
        </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template match="abbr|emph|expan|extref">
        <xsl:text> </xsl:text>
        <xsl:apply-templates/>
        <xsl:text> </xsl:text>
    </xsl:template>
    <xsl:template match="address">
        <xsl:for-each select="*">
            <xsl:apply-templates/>
            <xsl:if test="position() &lt; last()"><text> </text></xsl:if>
        </xsl:for-each>
    </xsl:template>
    <xsl:template name="bibref">
        <xsl:param name="bibrefs"/>
        <xsl:variable name="bibrefContent">
            <xsl:apply-templates select="$bibrefs/text() | $bibrefs/name | $bibrefs/title | $bibrefs/extref" />
        </xsl:variable>
        <dcterms:isReferencedBy>
            <xsl:attribute name="xml:lang" select="$languageUsedForDescription"/>
            <xsl:value-of select="fn:replace(normalize-space($bibrefContent), '[\n\t\r]', '')"/>
        </dcterms:isReferencedBy>
        <xsl:if test="$bibrefs/@xlink:href">
            <xsl:for-each select="$bibrefs">
                <dcterms:isReferencedBy>
                    <xsl:attribute name="rdf:resource" select="./@xlink:href"/>
                </dcterms:isReferencedBy>
            </xsl:for-each>
        </xsl:if>
        <xsl:if test="$bibrefs/extref/@xlink:href">
            <xsl:for-each select="$bibrefs/extref">
                <dcterms:isReferencedBy>
                    <xsl:attribute name="rdf:resource" select="./@xlink:href"/>
                </dcterms:isReferencedBy>
            </xsl:for-each>
        </xsl:if>
    </xsl:template>
    <xsl:template match="did/dao[not(@xlink:title='thumbnail') and not(@xlink:title='manifest') and not(@xlink:title='service')]" mode="firstLink">
        <xsl:choose>
            <xsl:when test="@href">
                <edm:isShownBy>
                    <xsl:attribute name="rdf:resource" select="@href"/>
                </edm:isShownBy>
                <edm:object>
                    <xsl:attribute name="rdf:resource" select="@href"/>
                </edm:object>
            </xsl:when>
            <xsl:when test="@xlink:href">
                <edm:isShownBy>
                    <xsl:attribute name="rdf:resource" select="@xlink:href"/>
                </edm:isShownBy>
                <edm:object>
                    <xsl:attribute name="rdf:resource" select="@xlink:href"/>
                </edm:object>
            </xsl:when>
        </xsl:choose>
    </xsl:template>
    <xsl:template match="did/dao[not(@xlink:title='thumbnail') and not(@xlink:title='service') and not(@xlink:title='manifest')]" mode="additionalLinks">
        <xsl:choose>
            <xsl:when test="@*:href != ''">
                <edm:hasView>
                    <xsl:attribute name="rdf:resource" select="@*:href"/>
                </edm:hasView>
            </xsl:when>
        </xsl:choose>
    </xsl:template>
    <xsl:template match="did/dao" mode="webResource">
        <xsl:param name="inheritedRightsInfo"/>
        <xsl:param name="manifestDao"/>
        <xsl:param name="serviceDao"/>
        <edm:WebResource>
            <xsl:attribute name="rdf:about">
                <xsl:choose>
                    <xsl:when test="@*:href">
                        <xsl:value-of select="@*:href"/>
                    </xsl:when>
                </xsl:choose>
            </xsl:attribute>
            <xsl:if test="$manifestDao and $manifestDao/@xlink:href and not($manifestDao/@xlink:href='')">
                <dcterms:isReferencedBy>
                    <xsl:attribute name="rdf:resource" select="$manifestDao/@xlink:href"/>
                </dcterms:isReferencedBy>
            </xsl:if>
            <xsl:if test="$serviceDao and $serviceDao/@xlink:href and not($serviceDao/@xlink:href='')">
                <svcs:has_service>
                    <xsl:attribute name="rdf:resource">
                        <xsl:value-of select="$serviceDao/@xlink:href"></xsl:value-of>
                    </xsl:attribute>
                </svcs:has_service>
            </xsl:if>
            <dc:description>
                <xsl:attribute name="xml:lang" select="$languageUsedForDescription"/>
                <xsl:choose>
                    <xsl:when test="@*:title != ''">
                        <xsl:value-of select="@*:title"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:value-of select="'Archival material'"/>
                    </xsl:otherwise>
                </xsl:choose>
            </dc:description>
            <xsl:if test="$dc_rights and normalize-space($dc_rights) != ''">
                <dc:rights>
                    <xsl:value-of select="$dc_rights"/>
                </dc:rights>
            </xsl:if>
            <xsl:choose>
                <xsl:when test="$useExistingRightsInfo = &quot;true&quot;">
                    <xsl:choose>
                        <xsl:when test="current()/../../userestrict[@type = 'dao']">
                            <xsl:call-template name="createRights">
                                <xsl:with-param name="rights"
                                                select="current()/../../userestrict[@type = 'dao']"/>
                            </xsl:call-template>
                        </xsl:when>
                        <xsl:when test="$inheritedRightsInfo != 'empty'">
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
                    <edm:rights>
                        <xsl:attribute name="rdf:resource">
                            <xsl:value-of select="$europeana_rights"/>
                        </xsl:attribute>
                    </edm:rights>
                </xsl:otherwise>
            </xsl:choose>
        </edm:WebResource>
        <xsl:if test="$serviceDao and $serviceDao/@xlink:href and not($serviceDao/@xlink:href='')">
            <svcs:Service>
                <xsl:attribute name="rdf:about">
                    <xsl:value-of select="$serviceDao/@xlink:href"></xsl:value-of>
                </xsl:attribute>
                <dcterms:conformsTo>
                    <xsl:attribute name="rdf:resource">http://iiif.io/api/image</xsl:attribute>
                </dcterms:conformsTo>
                <xsl:if test="$serviceDao/@xlink:arcrole and not($serviceDao/@xlink:arcrole='')">
                    <doap:implements>
                        <xsl:attribute name="rdf:resource">
                            <xsl:value-of select="$serviceDao/@xlink:arcrole"></xsl:value-of>
                        </xsl:attribute>
                    </doap:implements>
                </xsl:if>
            </svcs:Service>
        </xsl:if>
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
                    <xsl:if test="./@xlink:role">
                        <xsl:value-of select="did/dao[not(@xlink:title = 'thumbnail') and not(@xlink:title='manifest') and not(@xlink:title='service')][1]"/>
                    </xsl:if>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <xsl:choose>
            <xsl:when test="$link != ''">
                <edm:isShownBy>
                    <xsl:attribute name="rdf:resource" select="$link"/>
                </edm:isShownBy>
                <edm:object>
                    <xsl:attribute name="rdf:resource" select="$link"/>
                </edm:object>
            </xsl:when>
            <xsl:otherwise>
                <edm:object>
                    <xsl:attribute name="rdf:resource">
                        <xsl:if test="./@xlink:role">
                            <xsl:value-of select="did/dao[not(@xlink:title = 'thumbnail') and not(@xlink:title='manifest') and not(@xlink:title='service')][1]"/>
                        </xsl:if>
                    </xsl:attribute>
                </edm:object>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    <xsl:template match="entry">
        <xsl:if test="normalize-space(node()) != ''">
            <xsl:value-of select="node()"/>
        </xsl:if>
    </xsl:template>
    <xsl:template match="head">
        <xsl:if test="normalize-space(node()) != ''">
            <xsl:value-of select="node()"/>
            <xsl:text>: </xsl:text>
        </xsl:if>
    </xsl:template>
    <xsl:template match="item">
        <xsl:if test="normalize-space(node()) != ''">
            <xsl:value-of select="node()"/>
        </xsl:if>
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
    <xsl:template match="repository">
        <xsl:variable name="content">
            <xsl:apply-templates/>
        </xsl:variable>
        <edm:dataProvider>
            <xsl:value-of select="fn:replace(normalize-space($content), '[\n\t\r]', '')"/>
        </edm:dataProvider>
    </xsl:template>
    <xsl:template match="scopecontent">
        <xsl:variable name="content">
            <xsl:apply-templates select="head" />
            <xsl:for-each select="p | list/item | table">
                <xsl:apply-templates />
                <xsl:if test="position() &lt; last()"><xsl:text> </xsl:text></xsl:if>
            </xsl:for-each>
        </xsl:variable>
        <xsl:if test="fn:replace(normalize-space($content), '[\n\t\r]', '') != ''">
            <dc:description>
                <xsl:attribute name="xml:lang" select="$languageUsedForDescription"/>
                <xsl:value-of select="fn:replace(normalize-space($content), '[\n\t\r]', '')"/>
            </dc:description>
        </xsl:if>
    </xsl:template>
    <xsl:template match="unitdate">
        <xsl:choose>
            <xsl:when test="$useISODates='true'">
                <xsl:choose>
                    <xsl:when test="./@era=&quot;ce&quot; and ./@normal">
                        <xsl:analyze-string select="./@normal"
                                            regex="(\d\d\d\d(-?\d\d(-?\d\d)?)?)(/(\d\d\d\d(-?\d\d(-?\d\d)?)?))?">
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
                        <dcterms:created>
                            <xsl:apply-templates select="./@normal"/>
                        </dcterms:created>
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
                        <xsl:apply-templates select="./@normal"/>
                    </xsl:if>
                </xsl:if>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    <xsl:template match="unitid">
        <xsl:value-of select="fn:replace(normalize-space(.), '[\n\t\r]', '')"/>
    </xsl:template>
    <xsl:template match="unittitle">
        <xsl:variable name="content">
            <xsl:apply-templates/>
        </xsl:variable>
        <dc:title>
            <xsl:attribute name="xml:lang" select="$languageUsedForDescription"/>
            <xsl:value-of select="fn:replace(normalize-space($content), '[\n\t\r]', '')"/>
        </dc:title>
    </xsl:template>
    <xsl:template match="unittitle" mode="dcDescription">
        <xsl:variable name="content">
            <xsl:apply-templates/>
        </xsl:variable>
        <xsl:value-of select="fn:replace(normalize-space($content), '[\n\t\r]', '')"/>
    </xsl:template>
    <xsl:template match="@normal">
        <xsl:analyze-string select="."
                            regex="(\d\d\d\d)((-?\d\d)(-?\d\d))?(/(\d\d\d\d)((-?\d\d)(-?\d\d))?)?">
            <xsl:matching-substring>
                <dcterms:created>
                    <xsl:value-of select="regex-group(1)"/>
                    <xsl:if test="regex-group(2) != ''">
                        <xsl:choose>
                            <xsl:when test="contains(regex-group(2), '-')">
                                <xsl:value-of select="regex-group(3)"/>
                                <xsl:value-of select="regex-group(4)"/>
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:value-of select="concat('-', regex-group(3))"/>
                                <xsl:value-of select="concat('-', regex-group(4))"/>
                            </xsl:otherwise>
                        </xsl:choose>
                    </xsl:if>
                    <xsl:if test="regex-group(5) != ''">
                        <xsl:text>/</xsl:text>
                        <xsl:value-of select="regex-group(6)"/>
                        <xsl:if test="regex-group(7) != ''">
                            <xsl:choose>
                                <xsl:when test="contains(regex-group(7), '-')">
                                    <xsl:value-of select="regex-group(8)"/>
                                    <xsl:value-of select="regex-group(9)"/>
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:value-of select="concat('-', regex-group(8))"/>
                                    <xsl:value-of select="concat('-', regex-group(9))"/>
                                </xsl:otherwise>
                            </xsl:choose>
                        </xsl:if>
                    </xsl:if>
                </dcterms:created>
            </xsl:matching-substring>
        </xsl:analyze-string>
    </xsl:template>

    <xsl:template match="list | table | tgroup | tbody | row">
        <xsl:for-each select="*">
            <xsl:apply-templates />
            <xsl:if test="position() &lt; last() and normalize-space(.) != ''"><xsl:text> </xsl:text></xsl:if>
        </xsl:for-each>
    </xsl:template>

    <xsl:template match="bibref/imprint" />
    <xsl:template match="bibref/name | bibref/title">
        <xsl:if test="local-name() = 'title' and local-name(preceding-sibling::*[1]) = 'name'">
            <xsl:text>: </xsl:text>
        </xsl:if>
        <xsl:if test="local-name() = 'name' and local-name(preceding-sibling::*[1]) = 'title'">
            <xsl:text>: </xsl:text>
        </xsl:if>
        <xsl:if test="local-name() = 'name' and local-name(preceding-sibling::*[1]) = 'name'">
            <xsl:text>, </xsl:text>
        </xsl:if>
        <xsl:if test="local-name() = 'title' and local-name(preceding-sibling::*[1]) = 'title'">
            <xsl:text>, </xsl:text>
        </xsl:if>
        <xsl:apply-templates />
    </xsl:template>

    <xsl:template name="materialspec">
        <xsl:param name="materialspecs"/>
        <xsl:for-each select="$materialspecs">
            <dc:format>
                <xsl:attribute name="xml:lang" select="$languageUsedForDescription"/>
                <xsl:value-of select="."/>
            </dc:format>
        </xsl:for-each>
    </xsl:template>

</xsl:stylesheet>
