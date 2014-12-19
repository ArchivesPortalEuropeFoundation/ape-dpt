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

    <xsl:template match="/">
        <rdf:RDF>
            <edm:Agent>
                <xsl:attribute name="rdf:about">
                    <xsl:value-of select="concat('creator_', /eac-cpf/control/recordId)"/>
                </xsl:attribute>
                <xsl:apply-templates select="node()" mode="skos"/>
                <xsl:apply-templates select="node()" mode="dc"/>
                <xsl:apply-templates select="node()" mode="dcterms"/>
                <xsl:apply-templates select="node()" mode="edm"/>
                <xsl:apply-templates select="node()" mode="foaf"/>
                <xsl:apply-templates select="node()" mode="rdaGr2"/>
                <xsl:apply-templates select="node()" mode="owl"/>
            </edm:Agent>
        </rdf:RDF>
    </xsl:template>
    
    <xsl:template match="biogHist/abstract" mode="skos">
        <skos:note>
            <xsl:if test="@xml:lang">
                <xsl:attribute name="xml:lang" select="@xml:lang"/>
            </xsl:if>
            <xsl:value-of select="node()"/>
        </skos:note>
    </xsl:template>

    <xsl:template match="nameEntry[@localType = 'preferred']" name="preferredEntry" mode="skos">
        <xsl:variable name="content">
            <xsl:call-template name="createConcatenatedNameEntry"/>
        </xsl:variable>
        <xsl:choose>
            <xsl:when test="preceding-sibling::nameEntry[@localType = 'preferred']">
                <skos:altLabel>
                    <xsl:if test="@xml:lang">
                        <xsl:attribute name="xml:lang" select="@xml:lang"/>
                    </xsl:if>
                    <xsl:value-of select="$content"/>
                </skos:altLabel>
            </xsl:when>
            <xsl:otherwise>
                <skos:prefLabel>
                    <xsl:if test="@xml:lang">
                        <xsl:attribute name="xml:lang" select="@xml:lang"/>
                    </xsl:if>
                    <xsl:value-of select="$content"/>
                </skos:prefLabel>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    <xsl:template match="nameEntry[@localType = 'authorized']" name="authorizedEntry" mode="skos">
        <xsl:variable name="content">
            <xsl:call-template name="createConcatenatedNameEntry"/>
        </xsl:variable>
        <xsl:choose>
            <xsl:when test="./position() = 1">
                <skos:prefLabel>
                    <xsl:if test="@xml:lang">
                        <xsl:attribute name="xml:lang" select="@xml:lang"/>
                    </xsl:if>
                    <xsl:value-of select="$content"/>
                </skos:prefLabel>
            </xsl:when>
            <xsl:otherwise>
                <skos:altLabel>
                    <xsl:if test="@xml:lang">
                        <xsl:attribute name="xml:lang" select="@xml:lang"/>
                    </xsl:if>
                    <xsl:value-of select="$content"/>
                </skos:altLabel>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    <xsl:template match="nameEntry[not(@localType)]" name="untypedEntry" mode="skos">
        <xsl:variable name="content">
            <xsl:call-template name="createConcatenatedNameEntry"/>
        </xsl:variable>
        <xsl:choose>
            <xsl:when test="./position() = 1">
                <skos:prefLabel>
                    <xsl:if test="@xml:lang">
                        <xsl:attribute name="xml:lang" select="@xml:lang"/>
                    </xsl:if>
                    <xsl:value-of select="$content"/>
                </skos:prefLabel>
            </xsl:when>
            <xsl:otherwise>
                <skos:altLabel>
                    <xsl:if test="@xml:lang">
                        <xsl:attribute name="xml:lang" select="@xml:lang"/>
                    </xsl:if>
                    <xsl:value-of select="$content"/>
                </skos:altLabel>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    <xsl:template match="nameEntry[@localType = 'alternative'] | nameEntry[@localType = 'other'] | nameEntry[@localType = 'abbreviation']" name="otherEntries" mode="skos">
        <xsl:variable name="content">
            <xsl:call-template name="createConcatenatedNameEntry"/>
        </xsl:variable>
        <skos:altLabel>
            <xsl:if test="@xml:lang">
                <xsl:attribute name="xml:lang" select="@xml:lang"/>
            </xsl:if>
            <xsl:value-of select="$content"/>
        </skos:altLabel>
    </xsl:template>
    
    <xsl:template match="nameEntryParallel[@localType = 'preferred']" mode="skos">
        <xsl:for-each select="nameEntry">
            <xsl:call-template name="preferredEntry"/>
        </xsl:for-each>
    </xsl:template>
    
    <xsl:template match="nameEntryParallel[@localType = 'authorized']" mode="skos">
        <xsl:for-each select="nameEntry">
            <xsl:call-template name="authorizedEntry"/>
        </xsl:for-each>
    </xsl:template>
    
    <xsl:template match="nameEntryParallel[not(@localType)]" mode="skos">
        <xsl:for-each select="nameEntry">
            <xsl:call-template name="untypedEntry"/>
        </xsl:for-each>
    </xsl:template>
    
    <xsl:template match="nameEntryParallel[@localType = 'alternative'] | nameEntryParallel[@localType = 'other'] | nameEntryParallel[@localType = 'abbreviation']" mode="skos">
        <xsl:for-each select="nameEntry">
            <xsl:call-template name="otherEntries"/>
        </xsl:for-each>
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
    
    <xsl:template match="occupation/term" mode="rdaGr2">
        <rdaGr2:professionOrOccupation>
            <xsl:if test="@vocabularySource">
                <xsl:attribute name="rdf:resource" select="@vocabularySource"/>
            </xsl:if>
            <xsl:value-of select="normalize-space(node())"/>
        </rdaGr2:professionOrOccupation>
    </xsl:template>
    
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
    
    <xsl:template match="alternativeSet/setComponent/@xlink:href" mode="owl">
        <owl:sameAs>
            <xsl:attribute name="rdf:resource" select="."/>
        </owl:sameAs>
    </xsl:template>
    
    <xsl:template name="createConcatenatedNameEntry">
        <xsl:choose>
            <xsl:when test="part">
                <xsl:apply-templates select="part"/>
                <xsl:text>.</xsl:text>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="normalize-space(node())"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    <xsl:template match="part">
        <xsl:choose>
            <xsl:when test="@localType = 'corpname'">
                <xsl:value-of select="normalize-space(node())"/>
            </xsl:when>
            <xsl:when test="@localType = 'legalform'">
                <xsl:value-of select="concat(', ', normalize-space(node()))"/>
            </xsl:when>
            <xsl:when test="@localType = 'famname'">
                <xsl:value-of select="normalize-space(node())"/>
            </xsl:when>
            <xsl:when test="@localType = 'persname'">
                <xsl:value-of select="normalize-space(node())"/>
            </xsl:when>
            <xsl:when test="@localType = 'surname'">
                <xsl:value-of select="normalize-space(node())"/>
            </xsl:when>
            <xsl:when test="@localType = 'birthname'">
                <xsl:value-of select="concat(' (', normalize-space(node()), ')')"/>
            </xsl:when>
            <xsl:when test="@localType = 'prefix'">
                <xsl:value-of select="concat(', ', normalize-space(node()))"/>
            </xsl:when>
            <xsl:when test="@localType = 'firstname'">
                <xsl:value-of select="concat(', ', normalize-space(node()))"/>
            </xsl:when>
            <xsl:when test="@localType = 'patronymic'">
                <xsl:value-of select="concat(' ', normalize-space(node()))"/>
            </xsl:when>
            <xsl:when test="@localType = 'alias'">
                <xsl:value-of select="concat(', (alias: ', normalize-space(node()), ')')"/>
            </xsl:when>
            <xsl:when test="@localType = 'suffix'">
                <xsl:value-of select="concat(', ', normalize-space(node()))"/>
            </xsl:when>
            <xsl:when test="@localType = 'title'">
                <xsl:value-of select="concat(', ', normalize-space(node()))"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="concat(', ', normalize-space(node()))"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    <xsl:template match="cpfDescription | description | eac-cpf | identity | occupations | places | relations" mode="#all">
        <xsl:apply-templates select="node()" mode="#current"/>
    </xsl:template>
    
    <xsl:template match="abbreviation | agencyCode | agencyName | agent | agentType | citation 
        | control | conventionDeclaration | entityType | eventDateTime | eventType | language
        | languageDeclaration | maintenanceAgency | maintenanceEvent | maintenanceHistory
        | maintenanceStatus | recordId | script | source | sourceEntry | sources" mode="#all"/>
    
    <xsl:template match="text()" mode="#all"/>
</xsl:stylesheet>