<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:fo="http://www.w3.org/1999/XSL/Format"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:edm="http://www.europeana.eu/schemas/edm/"
    xmlns:enrichment="http://www.europeana.eu/schemas/edm/enrichment"
    xmlns:foaf="http://xmlns.com/foaf/0.1/"
    xmlns:owl="http://www.w3.org/2002/07/owl#"
    xmlns:wgs84="http://www.w3.org/2003/01/geo/wgs84_pos#"
    xmlns:skos="http://www.w3.org/2004/02/skos/core#"
    xmlns:ore="http://www.openarchives.org/ore/terms/"
    xmlns:oai="http://www.openarchives.org/OAI/2.0"
    xmlns:rdaGr2="http://rdvocab.info/ElementsGr2/"
    xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
    xmlns:dcterms="http://purl.org/dc/terms/" xmlns:dc="http://purl.org/dc/elements/1.1/"
    xmlns:europeana="http://www.europeana.eu/schemas/ese/"
    xmlns="http://www.europeana.eu/schemas/edm/" xpath-default-namespace="urn:isbn:1-931666-33-4"
    xmlns:fn="http://www.w3.org/2005/xpath-functions" xmlns:xlink="http://www.w3.org/1999/xlink"
    xsi:schemaLocation="http://www.w3.org/1999/02/22-rdf-syntax-ns# http://www.europeana.eu/schemas/edm/EDM.xsd"
    exclude-result-prefixes="xlink fo fn">
    <xsl:output method="xml" encoding="UTF-8" indent="yes"/>
    
    <xsl:variable name="nameEntries">
        <xsl:apply-templates select="/eac-cpf/cpfDescription/identity"></xsl:apply-templates>
    </xsl:variable>

    <xsl:template match="/">
        <rdf:RDF>
            <edm:Agent>
                <xsl:attribute name="rdf:about">
                    <xsl:value-of select="concat('creator_', /eac-cpf/control/recordId)"/>
                </xsl:attribute>
                <xsl:sequence select="$nameEntries/skos:prefLabel"/>
                <xsl:sequence select="$nameEntries/skos:altLabel"/>
                <xsl:apply-templates select="node()" mode="skos"/>
                <xsl:apply-templates select="node()" mode="dc"/>
                <xsl:apply-templates select="node()" mode="edm"/>
                <xsl:apply-templates select="node()" mode="foaf"/>
                <xsl:apply-templates select="node()" mode="rdaGr2"/>
                <xsl:apply-templates select="node()" mode="owl"/>
<!--                <xsl:apply-templates select="node()" mode="dcterms"/>-->
            </edm:Agent>
        </rdf:RDF>
    </xsl:template>
    
    <xsl:template match="identity">
        <xsl:variable name="allNameEntries" select="nameEntry | nameEntryParallel"/>
        <xsl:for-each-group select="$allNameEntries" group-by="part/@xml:lang or @xml:lang">
            <xsl:choose>
                <xsl:when test="current-grouping-key() = false()">
                    <xsl:for-each select="current-group()">
                        <xsl:choose>
                            <xsl:when test="position() = 1">
                                <xsl:element name="skos:prefLabel">
                                    <xsl:call-template name="createConcatenatedNameEntry">
                                        <xsl:with-param name="listName" select="."/>
                                    </xsl:call-template>
                                </xsl:element>
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:element name="skos:altLabel">
                                    <xsl:call-template name="createConcatenatedNameEntry">
                                        <xsl:with-param name="listName" select="."/>
                                    </xsl:call-template>
                                </xsl:element>
                            </xsl:otherwise>
                        </xsl:choose>
                    </xsl:for-each>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:for-each-group select="current-group()" group-by="descendant-or-self::node()/@xml:lang[1]" >
                        <xsl:variable name="language" select="current-grouping-key()"/>
                        <xsl:for-each select="current-group()">
                            <xsl:choose>
                                <xsl:when test="position() = 1">
                                    <xsl:element name="skos:prefLabel">
                                        <xsl:attribute name="xml:lang" select="$language"/>
                                        <xsl:call-template name="createConcatenatedNameEntry">
                                            <xsl:with-param name="listName" select="."/>
                                        </xsl:call-template>
                                    </xsl:element>
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:element name="skos:altLabel">
                                        <xsl:attribute name="xml:lang" select="$language"/>
                                        <xsl:call-template name="createConcatenatedNameEntry">
                                            <xsl:with-param name="listName" select="."/>
                                        </xsl:call-template>
                                    </xsl:element>
                                </xsl:otherwise>
                            </xsl:choose>
                        </xsl:for-each>
                    </xsl:for-each-group>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:for-each-group>
    </xsl:template>
    
    <xsl:template match="biogHist/abstract" mode="skos">
        <skos:note>
            <xsl:if test="@xml:lang">
                <xsl:attribute name="xml:lang" select="@xml:lang"/>
            </xsl:if>
            <xsl:value-of select="node()"/>
        </skos:note>
    </xsl:template>

    <xsl:template match="entityId" mode="dc">
        <dc:identifier>
            <xsl:value-of select="node()"/>
        </dc:identifier>
    </xsl:template>
    
    <xsl:template match="recordId" mode="dc">
        <xsl:if test="not(/eac-cpf/cpfDescription/identity/entityId)">
            <dc:identifier>
                <xsl:value-of select="node()"/>
            </dc:identifier>
        </xsl:if>
    </xsl:template>
    
    <xsl:template match="cpfRelation[@cpfRelationType = 'hierarchical-child']" mode="dcterms">
        <dcterms:hasPart>
            <xsl:attribute name="rdf:resource">
                <xsl:value-of select="@xlink:href"/>
            </xsl:attribute>
        </dcterms:hasPart>
    </xsl:template>
    
    <xsl:template match="cpfRelation[@cpfRelationType = 'hierarchical-parent']" mode="dcterms">
        <dcterms:isPartOf>
            <xsl:attribute name="rdf:resource">
                <xsl:value-of select="@xlink:href"/>
            </xsl:attribute>
        </dcterms:isPartOf>
    </xsl:template>
    
    <xsl:template match="place/placeEntry/@vocabularySource" mode="edm">
        <edm:hasMet>
            <xsl:attribute name="rdf:resource" select="normalize-space(.)"/>
        </edm:hasMet>
    </xsl:template>
    
    <xsl:template match="cpfRelation[@cpfRelationType = 'hierarchical'] | cpfRelation[@cpfRelationType = 'temporal'] | cpfRelation[@cpfRelationType = 'temporal-earlier'] | cpfRelation[@cpfRelationType = 'temporal-later'] | cpfRelation[@cpfRelationType = 'family'] | cpfRelation[@cpfRelationType = 'associative']" mode="edm">
        <edm:isRelatedTo>
            <xsl:attribute name="rdf:resource">
                <xsl:value-of select="@xlink:href"/>
            </xsl:attribute>
        </edm:isRelatedTo>
    </xsl:template>
    
    <xsl:template match="nameEntry[@localType = 'authorized']/part | nameEntry[@localType = 'preferred']/part | nameEntry[not(@localType)]/part" mode="foaf">
        <xsl:if test="parent::node()[not(@localType)]">
            <foaf:name>
                <xsl:value-of select="normalize-space(node())"/>
            </foaf:name>
        </xsl:if>
    </xsl:template>
    
    <xsl:template match="nameEntryParallel[@localType = 'authorized']/nameEntry/part | nameEntryParallel[@localType = 'preferred']/nameEntry/part | nameEntryParallel[not(@localType)]/nameEntry/part" mode="foaf">
        <xsl:if test="parent::node()[2][not(@localType)]">
            <foaf:name>
                <xsl:value-of select="normalize-space(node())"/>
            </foaf:name>
        </xsl:if>
    </xsl:template>
    
    <xsl:template match="existDates//date[1]" mode="rdaGr2">
        <xsl:if test="not(/eac-cpf/cpfDescription/description/existDates//dateRange)">
            <xsl:choose>
                <xsl:when test="/eac-cpf/cpfDescription/identity/entityType = 'person'">
                    <rdaGr2:dateOfBirth>
                        <xsl:value-of select="node()"/>
                    </rdaGr2:dateOfBirth>
                </xsl:when>
                <xsl:otherwise>
                    <rdaGr2:dateOfEstablishment>
                        <xsl:value-of select="node()"/>
                    </rdaGr2:dateOfEstablishment>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:if>
    </xsl:template>
    
    <xsl:template match="existDates//dateRange[1]/fromDate" mode="rdaGr2">
        <xsl:variable name="content">
            <xsl:choose>
                <xsl:when test="../@localType = ('unknown', 'unknownStart')">
                    <xsl:text>unknown</xsl:text>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="node()"/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <xsl:choose>
            <xsl:when test="/eac-cpf/cpfDescription/identity/entityType = 'person'">
                <rdaGr2:dateOfBirth>
                    <xsl:value-of select="$content"/>
                </rdaGr2:dateOfBirth>
            </xsl:when>
            <xsl:otherwise>
                <rdaGr2:dateOfEstablishment>
                    <xsl:value-of select="$content"/>
                </rdaGr2:dateOfEstablishment>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    <xsl:template match="existDates//dateRange[1]/toDate" mode="rdaGr2">
        <xsl:variable name="content">
            <xsl:choose>
                <xsl:when test="../@localType = ('unknown', 'unknownEnd')">
                    <xsl:text>unknown</xsl:text>
                </xsl:when>
                <xsl:when test="../@localType = ('open')">
                    <xsl:text>open</xsl:text>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="node()"/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <xsl:choose>
            <xsl:when test="/eac-cpf/cpfDescription/identity/entityType = 'person'">
                <rdaGr2:dateOfDeath>
                    <xsl:value-of select="$content"/>
                </rdaGr2:dateOfDeath>
            </xsl:when>
            <xsl:otherwise>
                <rdaGr2:dateOfTermination>
                    <xsl:value-of select="$content"/>
                </rdaGr2:dateOfTermination>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    <xsl:template match="occupations" mode="rdaGr2">
        <rdaGr2:professionOrOccupation>
            <xsl:if test="occupation/term/@vocabularySource">
                <xsl:attribute name="rdf:resource" select="occupation/term/@vocabularySource[1]"/>
            </xsl:if>
            <xsl:apply-templates mode="rdaGr2"/>
        </rdaGr2:professionOrOccupation>
    </xsl:template>
    
    <xsl:template match="occupation" mode="rdaGr2">
        <xsl:apply-templates mode="rdaGr2"/>
        <xsl:if test="following-sibling::occupation/term">
            <xsl:text>, </xsl:text>
        </xsl:if>
    </xsl:template>
    
    <xsl:template match="occupation/term" mode="rdaGr2">
        <xsl:value-of select="normalize-space(node())"/>
    </xsl:template>
    <!--    
    <xsl:template match="place/placeEntry[@localType = 'birth'][1]" mode="rdaGr2">
        <rdaGr2:placeOfBirth>
            <xsl:if test="@vocabularySource">
                <xsl:attribute name="rdf:resource" select="@vocabularySource"/>
            </xsl:if>
            <xsl:value-of select="node()"/>
        </rdaGr2:placeOfBirth>
    </xsl:template>
    
    <xsl:template match="place/placeEntry[@localType = 'death'][1]" mode="rdaGr2">
        <rdaGr2:placeOfDeath>
            <xsl:if test="@vocabularySource">
                <xsl:attribute name="rdf:resource" select="@vocabularySource"/>
            </xsl:if>
            <xsl:value-of select="node()"/>
        </rdaGr2:placeOfDeath>
    </xsl:template>
-->    
    <xsl:template match="alternativeSet/setComponent/@xlink:href" mode="owl">
        <owl:sameAs>
            <xsl:attribute name="rdf:resource" select="."/>
        </owl:sameAs>
    </xsl:template>
    
    <xsl:template name="createConcatenatedNameEntry">
        <xsl:param name="listName"/>
        <xsl:call-template name="nameEntryCreator">
            <xsl:with-param name="listName" select="$listName"/>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="part">
        <xsl:value-of select="normalize-space(node())"/>
    </xsl:template>
    
    <!-- template nameEntryCreator -->
    <xsl:template name="nameEntryCreator">
        <xsl:param name="listName"/>
        <xsl:variable name="firstName" select="$listName//part[@localType='firstname']"/>
        <xsl:variable name="surName" select="$listName//part[@localType='surname']"/>
        <xsl:variable name="patronymic" select="$listName//part[@localType='patronymic']"/>
        <xsl:variable name="prefix" select="$listName//part[@localType='prefix']"/>
        <xsl:variable name="suffix" select="$listName//part[@localType='suffix']"/>
        <xsl:variable name="alias" select="$listName//part[@localType='alias']"/>
        <xsl:variable name="title" select="$listName//part[@localType='title']"/>
        <xsl:variable name="birthname" select="$listName//part[@localType='birthname']"/>
        <xsl:variable name="legalform" select="$listName//part[@localType='legalform']"/>
        <xsl:variable name="corpname" select="$listName//part[@localType='corpname']"/>
        <xsl:variable name="famname" select="$listName//part[@localType='famname']"/>
        <xsl:variable name="persname" select="$listName//part[@localType='persname']"/>
        <xsl:choose>
            <xsl:when test="not($corpname) and not($famname) and not($persname) and not($legalform) and not($listName//part[not(@localType) or @localType=''])"> 
                <xsl:if test="$surName">
                    <xsl:for-each select="$surName">
                        <xsl:apply-templates select="."/>
                        <xsl:if test="position()!=last()">
                            <xsl:text> </xsl:text>
                        </xsl:if>
                    </xsl:for-each>	
                    <xsl:if test="$birthname">
                        <xsl:text> </xsl:text>
                    </xsl:if>
                </xsl:if>
                <xsl:if test="$birthname">
                    <xsl:text>(</xsl:text>
                    <xsl:for-each select="$birthname"> 	
                        <xsl:apply-templates select="."/>
                        <xsl:if test="position()!=last()">
                            <xsl:text> </xsl:text>
                        </xsl:if>
                    </xsl:for-each>	  
                    <xsl:text>)</xsl:text>
                </xsl:if>
                <xsl:if test="$prefix">
                    <xsl:if test="$surName or $birthname">
                        <xsl:text>, </xsl:text>
                    </xsl:if>
                    <xsl:for-each select="$prefix"> 	
                        <xsl:apply-templates select="."/>
                        <xsl:if test="position()!=last()">
                            <xsl:text> </xsl:text>
                        </xsl:if>
                    </xsl:for-each>	  
                </xsl:if>
                <xsl:if test="$firstName">
                    <xsl:if test="$surName or $birthname or $prefix">
                        <xsl:text>, </xsl:text>
                    </xsl:if>
                    <xsl:for-each select="$firstName"> 	
                        <xsl:apply-templates select="."/>
                        <xsl:if test="position()!=last()">
                            <xsl:text> </xsl:text>
                        </xsl:if>
                    </xsl:for-each>	
                </xsl:if>
                <xsl:if test="$patronymic">
                    <xsl:choose>
                        <xsl:when test="$firstName">
                            <xsl:text> </xsl:text>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:if test="$surName or $birthname or $prefix">
                                <xsl:text>, </xsl:text>
                            </xsl:if>
                        </xsl:otherwise>
                    </xsl:choose>
                    <xsl:for-each select="$patronymic"> 	
                        <xsl:apply-templates select="."/>
                        <xsl:if test="position()!=last()">
                            <xsl:text> </xsl:text>
                        </xsl:if>
                    </xsl:for-each>	
                </xsl:if>
                <xsl:if test="$suffix">
                    <xsl:if test="$surName or $birthname or $prefix or $firstName or $patronymic">
                        <xsl:text>, </xsl:text>
                    </xsl:if>
                    <xsl:for-each select="$suffix"> 	
                        <xsl:apply-templates select="."/>
                        <xsl:if test="position()!=last()">
                            <xsl:text> </xsl:text>
                        </xsl:if>
                    </xsl:for-each>	
                </xsl:if>
                <xsl:if test="$title">
                    <xsl:if test="$surName or $birthname or $prefix or $firstName or $patronymic or $suffix">
                        <xsl:text>, </xsl:text>
                    </xsl:if>
                    <xsl:for-each select="$title"> 	
                        <xsl:apply-templates select="."/>
                        <xsl:if test="position()!=last()">
                            <xsl:text> </xsl:text>
                        </xsl:if>
                    </xsl:for-each>	
                </xsl:if>
            </xsl:when>
            <xsl:otherwise>
                <xsl:choose>
                    <xsl:when test="$corpname and $legalform">
                        <xsl:apply-templates select="$corpname"/>
                        <xsl:text> </xsl:text>
                        <xsl:apply-templates select="$legalform"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:apply-templates select="$listName//part[1]"/>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:otherwise>
        </xsl:choose>
        <xsl:if test="$alias">
            <xsl:if test="$surName or $birthname or $prefix or $firstName or $patronymic or $suffix or $title or $corpname or $famname or $persname">
                <xsl:text> </xsl:text> 
            </xsl:if>
            <xsl:text>(alias: </xsl:text>
            <xsl:for-each select="$alias"> 	
                <xsl:apply-templates select="."/>
                <xsl:if test="position()!=last()">
                    <xsl:text> </xsl:text>
                </xsl:if>
            </xsl:for-each>	
            <xsl:text>)</xsl:text>
        </xsl:if>
    </xsl:template>
    
    <xsl:template match="cpfDescription | description | eac-cpf | places | relations" mode="#all">
        <xsl:apply-templates select="node()" mode="#current"/>
    </xsl:template>
    
    <xsl:template match="abbreviation | agencyCode | agencyName | agent | agentType | citation 
        | control | conventionDeclaration | entityType | eventDateTime | eventType | language
        | languageDeclaration | maintenanceAgency | maintenanceEvent | maintenanceHistory
        | maintenanceStatus | recordId | script | source | sourceEntry | sources" mode="#all"/>
    
    <xsl:template match="text()" mode="#all"/>
</xsl:stylesheet>