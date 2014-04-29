<?xml version="1.0" encoding="UTF-8"?>
<!--
        EAC-CPF default conversion into APE-EAC-CPF
-->
<xsl:stylesheet version="2.0" xmlns="urn:isbn:1-931666-33-4"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:none="none"
    xmlns:ape="http://www.archivesportaleurope.net/functions"
    xmlns:xlink="http://www.w3.org/1999/xlink"
    xsi:schemaLocation="urn:isbn:1-931666-33-4 http://eac.staatsbibliothek-berlin.de/schema/cpf.xsd"
    xpath-default-namespace="urn:isbn:1-931666-33-4" exclude-result-prefixes="xsl fo xs none ape">

    <xsl:param name="recordId" select="''"/>
    <xsl:param name="mainagencycode" select="''"/>

    <xsl:output indent="yes" method="xml"/>
    <xsl:strip-space elements="*"/>

    <!-- The root element -->
    <xsl:template match="/">
        <xsl:apply-templates select="node()" mode="top"/>
    </xsl:template>

    <!--
      copy all text nodes
      node() and text() have the same priority in XSLT 1.0 and 2.0
      to avoid "Recoverable error: XTRE0540: Ambiguous rule match for..."
      it is better to provide a priority
    -->
    <xsl:template match="text()" mode="#all" priority="2">
        <xsl:choose>
            <xsl:when test="contains(., '&#xa;')">
                <xsl:value-of select="translate(normalize-space(.), '&#xa;', ' ')"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="."/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <!-- eac-cpf -->
    <xsl:template match="eac-cpf" name="eac-cpf" mode="top">
        <eac-cpf xmlns="urn:isbn:1-931666-33-4" xmlns:xlink="http://www.w3.org/1999/xlink"
            xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="urn:isbn:1-931666-33-4 http://www.archivesportaleurope.net/Portal/profiles/apeEAC-CPF.xsd http://www.w3.org/1999/xlink http://www.loc.gov/standards/xlink/xlink.xsd">
            <xsl:if test="@xml:base">
                <xsl:attribute name="xml:base" select="@xml:base"/>
            </xsl:if>
            <xsl:if test="@xml:lang">
                <xsl:attribute name="xml:lang" select="@xml:lang"/>
            </xsl:if>
            <xsl:apply-templates select="node()" mode="copy"/>
        </eac-cpf>
    </xsl:template>

    <!-- First, all elements used as subelements in several spots are listed:
    - everything in relation to dates (everywhere where applicable)
    - everything in relation to places (everywhere where applicable)
    - term (localControl, localDescription, legalStatus, function, occupation, mandate)
    - citation (everywhere where applicable)
    - descriptiveNote (everywhere where applicable) -->
    <!-- useDates -->
    <xsl:template match="useDates" mode="copy">
        <useDates>
            <xsl:choose>
                <xsl:when test="count(date) = 1 and count(dateRange) = 0">
                    <xsl:apply-templates select="date" mode="copy"/>
                </xsl:when>
                <xsl:when test="count(dateRange) = 1 and count(date) = 0">
                    <xsl:apply-templates select="dateRange" mode="copy"/>
                </xsl:when>
                <xsl:otherwise>
                    <dateSet>
                        <xsl:for-each select="date | dateRange | dateSet/date | dateSet/dateRange">
                            <xsl:apply-templates select="." mode="copy"/>
                        </xsl:for-each>
                    </dateSet>
                </xsl:otherwise>
            </xsl:choose>
        </useDates>
    </xsl:template>

    <!-- date -->
    <xsl:template match="date" name="singleDate" mode="#all">
        <date>
            <xsl:if test="@notAfter">
                <xsl:attribute name="notAfter" select="@notAfter"/>
            </xsl:if>
            <xsl:if test="@notBefore">
                <xsl:attribute name="notBefore" select="@notBefore"/>
            </xsl:if>
            <xsl:if test="@xml:lang">
                <xsl:attribute name="xml:lang" select="@xml:lang"/>
            </xsl:if>
            <xsl:choose>
                <xsl:when test="string-length(.)!=0">
                    <xsl:if test="@standardDate">
                        <xsl:attribute name="standardDate" select="@standardDate"/>
                    </xsl:if>
                    <xsl:value-of select="."/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:attribute name="standardDate" select="'2099'"/>
                    <xsl:value-of select="'unknown'"/>
                </xsl:otherwise>
            </xsl:choose>
        </date>
    </xsl:template>

    <!-- dateRange -->
    <xsl:template match="dateRange" name="singleDateRange" mode="#all">
        <dateRange>
            <fromDate>
                <xsl:if test="fromDate/@notAfter">
                    <xsl:attribute name="notAfter" select="fromDate/@notAfter"/>
                </xsl:if>
                <xsl:if test="fromDate/@notBefore">
                    <xsl:attribute name="notBefore" select="fromDate/@notBefore"/>
                </xsl:if>
                <xsl:if test="fromDate/@xml:lang">
                    <xsl:attribute name="xml:lang" select="fromDate/@xml:lang"/>
                </xsl:if>
                <xsl:choose>
                    <xsl:when test="string-length(fromDate)!=0">
                        <xsl:if test="fromDate/@standardDate">
                            <xsl:attribute name="standardDate" select="fromDate/@standardDate"/>
                        </xsl:if>
                        <xsl:value-of select="fromDate"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:attribute name="standardDate" select="'0001'"/>
                        <xsl:value-of select="'unknown'"/>
                    </xsl:otherwise>
                </xsl:choose>
            </fromDate>
            <toDate>
                <xsl:if test="toDate/@notAfter">
                    <xsl:attribute name="notAfter" select="toDate/@notAfter"/>
                </xsl:if>
                <xsl:if test="toDate/@notBefore">
                    <xsl:attribute name="notBefore" select="toDate/@notBefore"/>
                </xsl:if>
                <xsl:if test="toDate/@xml:lang">
                    <xsl:attribute name="xml:lang" select="toDate/@xml:lang"/>
                </xsl:if>
                <xsl:choose>
                    <xsl:when test="string-length(toDate)!=0">
                        <xsl:if test="toDate/@standardDate">
                            <xsl:attribute name="standardDate" select="toDate/@standardDate"/>
                        </xsl:if>
                        <xsl:value-of select="toDate"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:attribute name="standardDate" select="'2099'"/>
                        <xsl:value-of select="'unknown'"/>
                    </xsl:otherwise>
                </xsl:choose>
            </toDate>
        </dateRange>
    </xsl:template>

    <!-- placeEntry -->
    <xsl:template match="placeEntry" mode="#all">
        <placeEntry>
            <xsl:if test="@countryCode">
                <xsl:attribute name="countryCode" select="@countryCode"/>
            </xsl:if>
            <xsl:if test="@latitude">
                <xsl:attribute name="latitude" select="@latitude"/>
            </xsl:if>
            <xsl:if test="@localType">
                <xsl:attribute name="localType" select="@localType"/>
            </xsl:if>
            <xsl:if test="@longitude">
                <xsl:attribute name="longitude" select="@longitude"/>
            </xsl:if>
            <xsl:if test="@scriptCode">
                <xsl:attribute name="scriptCode" select="@scriptCode"/>
            </xsl:if>
            <xsl:if test="@transliteration">
                <xsl:attribute name="transliteration" select="@transliteration"/>
            </xsl:if>
            <xsl:if test="@vocabularySource">
                <xsl:attribute name="vocabularySource" select="@vocabularySource"/>
            </xsl:if>
            <xsl:if test="@xml:lang">
                <xsl:attribute name="xml:lang" select="@xml:lang"/>
            </xsl:if>
            <xsl:value-of select="."/>
        </placeEntry>
    </xsl:template>

    <!-- term -->
    <xsl:template match="term" mode="#all">
        <term>
            <xsl:if test="@lastDateTimeVerified">
                <xsl:attribute name="lastDateTimeVerified" select="@lastDateTimeVerified"/>
            </xsl:if>
            <xsl:if test="@xml:scriptCode">
                <xsl:attribute name="scriptCode" select="@scriptCode"/>
            </xsl:if>
            <xsl:if test="@transliteration">
                <xsl:attribute name="transliteration" select="@transliteration"/>
            </xsl:if>
            <xsl:if test="@vocabularySource">
                <xsl:attribute name="vocabularySource" select="@vocabularySource"/>
            </xsl:if>
            <xsl:if test="@xml:lang">
                <xsl:attribute name="xml:lang" select="@xml:lang"/>
            </xsl:if>
            <xsl:value-of select="."/>
        </term>
    </xsl:template>

    <!-- citation (everywhere except structureOrGenealogy -->
    <xsl:template match="citation[not(parent::structureOrGenealogy)]" mode="#all">
        <citation>
            <xsl:if test="@lastDateTimeVerified">
                <xsl:attribute name="lastDateTimeVerified" select="@lastDateTimeVerified"/>
            </xsl:if>
            <xsl:attribute name="xlink:type" select="'simple'"/>
            <xsl:if test="@xlink:href">
                <xsl:attribute name="xlink:href" select="@xlink:href"/>
            </xsl:if>
            <xsl:if test="@xlink:role">
                <xsl:attribute name="xlink:role" select="@xlink:role"/>
            </xsl:if>
            <xsl:if test="@xlink:title">
                <xsl:attribute name="xlink:title" select="@xlink:title"/>
            </xsl:if>
            <xsl:value-of select="."/>
        </citation>
    </xsl:template>

    <!-- descriptiveNote -->
    <xsl:template match="descriptiveNote" name="createDescriptiveNote" mode="#all">
        <xsl:if test="*[text()]">
            <descriptiveNote>
                <xsl:for-each select="p">
                    <p>
                        <xsl:if test="@xml:lang">
                            <xsl:attribute name="xml:lang" select="@xml:lang"/>
                        </xsl:if>
                        <xsl:value-of select="."/>
                    </p>
                </xsl:for-each>
            </descriptiveNote>
        </xsl:if>
    </xsl:template>

    <!-- All elements specific for the control section are listed below -->

    <!-- control -->
    <xsl:template match="control" mode="copy">
        <control>
            <xsl:call-template name="apeRecordId"/>
            <xsl:apply-templates select="node()" mode="copy"/>
        </control>
    </xsl:template>

    <!-- APE recordId -->
    <xsl:template name="apeRecordId">
        <recordId>
            <xsl:value-of select="$recordId"/>
        </recordId>
    </xsl:template>

    <!-- existing recordId (will be moved to otherRecordId)-->
    <xsl:template match="recordId | otherRecordId" mode="copy">
        <otherRecordId>
            <xsl:choose>
                <xsl:when test="name(.) = 'recordId'">
                    <xsl:attribute name="localType">
                        <xsl:text>localIDconverted</xsl:text>
                    </xsl:attribute>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:if test="@localType">
                        <xsl:attribute name="localType" select="@localType"/>
                    </xsl:if>
                </xsl:otherwise>
            </xsl:choose>
            <xsl:value-of select="."/>
        </otherRecordId>
    </xsl:template>

    <!-- maintenanceAgency -->
    <xsl:template match="maintenanceAgency" mode="copy">
        <maintenanceAgency>
            <xsl:if test="not(agencyCode)">
                <xsl:call-template name="addApeAgencyCode"/>
            </xsl:if>
            <xsl:apply-templates select="node()" mode="copy"/>
        </maintenanceAgency>
    </xsl:template>

    <!-- agencyCode (should be ISIL code of institution) -->
    <xsl:template match="agencyCode" name="addApeAgencyCode" mode="copy">
        <agencyCode>
            <!-- IF agencyCode is ISIL code, leave as is, otherwise use
            $mainagencycode and move possible other code to "otherAgencyCode" -->
            <xsl:choose>
                <xsl:when test="text() = $mainagencycode">
                    <xsl:value-of select="text()"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="$mainagencycode"/>
                </xsl:otherwise>
            </xsl:choose>
        </agencyCode>
        <!-- IF agencyCode code is no ISIL, convert to otherAgencyCode -->
        <xsl:if test="text()[string-length(normalize-space(.)) > 0] and text() != $mainagencycode">
            <otherAgencyCode>
                <xsl:attribute name="localType">
                    <xsl:value-of select="'localIDconverted'"/>
                </xsl:attribute>
                <xsl:value-of select="text()"/>
            </otherAgencyCode>
        </xsl:if>
    </xsl:template>

    <!-- otherAgencyCode -->
    <xsl:template match="otherAgencyCode" mode="copy">
        <otherAgencyCode>
            <xsl:choose>
                <xsl:when test="@localType">
                    <xsl:attribute name="localType" select="@localType"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:attribute name="localType" select="'otherAgencyCode'"/>
                </xsl:otherwise>
            </xsl:choose>
            <xsl:value-of select="."/>
        </otherAgencyCode>
    </xsl:template>

    <!-- agencyName -->
    <xsl:template match="agencyName" mode="copy">
        <agencyName>
            <xsl:if test="@xml:lang">
                <xsl:attribute name="xml:lang" select="@xml:lang"/>
            </xsl:if>
            <xsl:value-of select="."/>
        </agencyName>
    </xsl:template>

    <!-- maintenanceStatus; is always "derived" after a conversion -->
    <xsl:template match="maintenanceStatus" mode="#all">
        <maintenanceStatus>derived</maintenanceStatus>
    </xsl:template>

    <!-- maintenanceHistory -->
    <xsl:template match="maintenanceHistory" mode="copy">
        <xsl:copy>
            <xsl:apply-templates select="node()" mode="copy"/>
            <maintenanceEvent>
                <eventType>
                    <xsl:text>derived</xsl:text>
                </eventType>
                <eventDateTime>
                    <xsl:attribute name="standardDateTime"
                        select="format-date(current-date(), '[Y0001]-[M01]-[D01]')"/>
                    <xsl:value-of select="format-date(current-date(), '[Y0001]-[M01]-[D01]')"/>
                </eventDateTime>
                <agentType>
                    <xsl:text>machine</xsl:text>
                </agentType>
                <agent>
                    <xsl:text>Archives Portal Europe</xsl:text>
                </agent>
            </maintenanceEvent>
        </xsl:copy>
    </xsl:template>

    <!-- maintenanceEvent -->
    <xsl:template match="maintenanceEvent" mode="copy">
        <xsl:copy>
            <xsl:apply-templates select="node()" mode="copy"/>
        </xsl:copy>
    </xsl:template>

    <!-- agent -->
    <xsl:template match="agent" mode="copy">
        <agent>
            <xsl:if test="@xml:lang">
                <xsl:attribute name="xml:lang" select="@xml:lang"/>
            </xsl:if>
            <xsl:value-of select="."/>
        </agent>
    </xsl:template>

    <!-- agentType -->
    <xsl:template match="agentType" mode="copy">
        <agentType>
            <xsl:value-of select="."/>
        </agentType>
    </xsl:template>

    <!-- eventDateTime -->
    <xsl:template match="eventDateTime" mode="copy">
        <eventDateTime>
            <xsl:if test="@standardDateTime">
                <xsl:attribute name="standardDateTime" select="@standardDateTime"/>
            </xsl:if>
            <xsl:value-of select="."/>
        </eventDateTime>
    </xsl:template>

    <!-- eventDescription -->
    <xsl:template match="eventDescription" mode="copy">
        <eventDescription>
            <xsl:if test="@xml:lang">
                <xsl:attribute name="xml:lang" select="@xml:lang"/>
            </xsl:if>
            <xsl:value-of select="."/>
        </eventDescription>
    </xsl:template>

    <!-- eventType -->
    <xsl:template match="eventType" mode="copy">
        <eventType>
            <xsl:value-of select="."/>
        </eventType>
    </xsl:template>

    <!-- publicationStatus -->
    <xsl:template match="publicationStatus" mode="copy">
        <publicationStatus>
            <xsl:value-of select="."/>
        </publicationStatus>
    </xsl:template>

    <!-- languageDeclaration -->
    <xsl:template match="languageDeclaration" mode="copy">
        <languageDeclaration>
            <xsl:apply-templates select="node()" mode="copy"/>
        </languageDeclaration>
    </xsl:template>

    <!-- language -->
    <xsl:template match="language" mode="copy">
        <language>
            <xsl:attribute name="languageCode" select="@languageCode"/>
            <xsl:if test="@xml:lang">
                <xsl:attribute name="xml:lang" select="@xml:lang"/>
            </xsl:if>
            <xsl:value-of select="."/>
        </language>
    </xsl:template>

    <!-- script -->
    <xsl:template match="script" mode="copy">
        <script>
            <xsl:attribute name="scriptCode" select="@scriptCode"/>
            <xsl:if test="@xml:lang">
                <xsl:attribute name="xml:lang" select="@xml:lang"/>
            </xsl:if>
            <xsl:value-of select="."/>
        </script>
    </xsl:template>

    <!-- sources -->
    <xsl:template match="sources" mode="copy">
        <sources>
            <xsl:apply-templates select="node()" mode="copy"/>
        </sources>
    </xsl:template>

    <!-- source -->
    <xsl:template match="source" mode="copy">
        <source>
            <xsl:if test="@lastDateTimeVerified">
                <xsl:attribute name="lastDateTimeVerified" select="@lastDateTimeVerified"/>
            </xsl:if>
            <xsl:attribute name="xlink:type" select="'simple'"/>
            <xsl:if test="@xlink:href">
                <xsl:attribute name="xlink:href" select="@xlink:href"/>
            </xsl:if>
            <xsl:if test="@xlink:role">
                <xsl:attribute name="xlink:role" select="@xlink:role"/>
            </xsl:if>
            <xsl:if test="@xlink:title">
                <xsl:attribute name="xlink:title" select="@xlink:title"/>
            </xsl:if>
            <xsl:apply-templates select="node()" mode="copy"/>
        </source>
    </xsl:template>

    <!-- sourceEntry -->
    <xsl:template match="sourceEntry" mode="copy">
        <sourceEntry>
            <xsl:if test="@scriptCode">
                <xsl:attribute name="scriptCode" select="@scriptCode"/>
            </xsl:if>
            <xsl:if test="@transliteration">
                <xsl:attribute name="transliteration" select="@transliteration"/>
            </xsl:if>
            <xsl:if test="@xlink:lang">
                <xsl:attribute name="xlink:lang" select="@xlink:lang"/>
            </xsl:if>
            <xsl:value-of select="."/>
        </sourceEntry>
    </xsl:template>

    <!-- conventionDeclaration -->
    <xsl:template match="conventionDeclaration" mode="copy">
        <conventionDeclaration>
            <xsl:apply-templates select="node()" mode="copy"/>
        </conventionDeclaration>
    </xsl:template>

    <!-- abbreviation -->
    <xsl:template match="abbreviation" mode="copy">
        <abbreviation>
            <xsl:value-of select="."/>
        </abbreviation>
    </xsl:template>

    <!-- localControl -->
    <xsl:template match="localControl" mode="copy">
        <localControl>
            <xsl:attribute name="localType">
                <xsl:choose>
                    <xsl:when test="@localType">
                        <xsl:value-of select="@localType"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:text>other</xsl:text>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:attribute>
            <xsl:apply-templates select="node()" mode="copy"/>
        </localControl>
    </xsl:template>

    <!-- localTypeDeclaration -->
    <xsl:template match="localTypeDeclaration" mode="copy">
        <localTypeDeclaration>
            <xsl:apply-templates select="node()" mode="copy"/>
        </localTypeDeclaration>
    </xsl:template>

    <!-- cpfDescription -->
    <xsl:template match="cpfDescription" mode="copy">
        <cpfDescription>
            <xsl:apply-templates select="identity" mode="copy"/>
            <xsl:choose>
                <xsl:when test="not(description)">
                    <description>
                        <existDates>
                            <dateRange>
                                <fromDate>
                                    <xsl:attribute name="standardDate">
                                        <xsl:text>0001</xsl:text>
                                    </xsl:attribute>
                                </fromDate>
                                <toDate>
                                    <xsl:attribute name="standardDate">
                                        <xsl:text>2099</xsl:text>
                                    </xsl:attribute>
                                </toDate>
                            </dateRange>
                        </existDates>
                    </description>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:apply-templates select="description" mode="copy"/>
                </xsl:otherwise>
            </xsl:choose>
            <xsl:apply-templates select="relations" mode="copy"/>
            <xsl:apply-templates select="alternativeSet" mode="copy"/>
        </cpfDescription>
    </xsl:template>

    <!-- identity -->
    <xsl:template match="identity" mode="copy">
        <identity>
            <xsl:if test="@identityType">
                <xsl:attribute name="identityType" select="@identityType"/>
            </xsl:if>
            <xsl:apply-templates select="node()" mode="copy"/>
        </identity>
    </xsl:template>

    <!-- nameEntryParallel -->
    <xsl:template match="nameEntryParallel" mode="copy">
        <nameEntryParallel>
            <xsl:if test="@localType">
                <xsl:attribute name="localType" select="@localType"/>
            </xsl:if>
            <xsl:apply-templates select="node()" mode="copy"/>
        </nameEntryParallel>
    </xsl:template>

    <!-- authorizedForm -->
    <xsl:template match="authorizedForm" mode="copy">
        <authorizedForm>
            <xsl:value-of select="."/>
        </authorizedForm>
    </xsl:template>

    <!-- alternativeForm -->
    <xsl:template match="alternativeForm" mode="copy">
        <alternativeForm>
            <xsl:value-of select="."/>
        </alternativeForm>
    </xsl:template>

    <!-- nameEntry -->
    <xsl:template match="nameEntry" mode="copy">
        <nameEntry>
            <xsl:if test="parent::identity">
                <xsl:if test="@localType">
                    <xsl:attribute name="localType">
                        <xsl:choose>
                            <xsl:when test="@localType = 'autorisée'">
                                <xsl:value-of>authorized</xsl:value-of>
                            </xsl:when>
                            <xsl:when test="@localType = 'variante'">
                                <xsl:value-of>alternative</xsl:value-of>
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:value-of select="@localType"/>
                            </xsl:otherwise>
                        </xsl:choose>
                    </xsl:attribute>
                </xsl:if>
                <xsl:if test="@transliteration">
                    <xsl:attribute name="transliteration" select="@transliteration"/>
                </xsl:if>
            </xsl:if>
            <xsl:apply-templates select="node()" mode="copy"/>
        </nameEntry>
    </xsl:template>

    <!-- part -->
    <xsl:template match="part" mode="copy">
        <part>
            <xsl:choose>
                <xsl:when test="@xml:lang">
                    <xsl:attribute name="xml:lang" select="@xml:lang"/>
                </xsl:when>
                <xsl:when test="../@xml:lang">
                    <xsl:attribute name="xml:lang" select="../@xml:lang"/>
                </xsl:when>
                <xsl:when test="../../@xml:lang">
                    <xsl:attribute name="xml:lang" select="../../@xml:lang"/>
                </xsl:when>
                <xsl:otherwise/>
            </xsl:choose>
            <xsl:value-of select="."/>
        </part>
    </xsl:template>

    <!-- preferredForm -->
    <xsl:template match="preferredForm" mode="copy">
        <preferredForm>
            <xsl:value-of select="."/>
        </preferredForm>
    </xsl:template>

    <!-- entityId -->
    <xsl:template match="entityId" mode="copy">
        <entityId>
            <xsl:if test="@localType">
                <xsl:attribute name="localType" select="@localType"/>
            </xsl:if>
            <xsl:value-of select="."/>
        </entityId>
    </xsl:template>

    <!-- entityType -->
    <xsl:template match="entityType" mode="copy">
        <entityType>
            <xsl:value-of select="."/>
        </entityType>
    </xsl:template>

    <!-- description -->
    <xsl:template match="description" mode="copy">
        <description>
            <xsl:choose>
                <xsl:when test="not(existDates)">
                    <existDates>
                        <dateRange>
                            <fromDate>
                                <xsl:attribute name="standardDate">
                                    <xsl:text>0001</xsl:text>
                                </xsl:attribute>
                            </fromDate>
                            <toDate>
                                <xsl:attribute name="standardDate">
                                    <xsl:text>2099</xsl:text>
                                </xsl:attribute>
                            </toDate>
                        </dateRange>
                    </existDates>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:apply-templates select="existDates" mode="copy"/>
                </xsl:otherwise>
            </xsl:choose>
            <xsl:choose>
                <xsl:when test="count(place) &gt; 0">
                    <places>
                        <xsl:for-each select="place">
                            <xsl:apply-templates select="." mode="copy"/>
                        </xsl:for-each>
                    </places>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:apply-templates select="places" mode="copy"/>
                </xsl:otherwise>
            </xsl:choose>
            <xsl:choose>
                <xsl:when test="count(localDescription) &gt; 0">
                    <localDescriptions>
                        <xsl:for-each select="localDescription">
                            <xsl:apply-templates select="." mode="copy"/>
                        </xsl:for-each>
                    </localDescriptions>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:apply-templates select="localDescriptions" mode="copy"/>
                </xsl:otherwise>
            </xsl:choose>
            <xsl:choose>
                <xsl:when test="count(legalStatus) &gt; 0">
                    <legalStatuses>
                        <xsl:for-each select="legalStatus">
                            <xsl:apply-templates select="." mode="copy"/>
                        </xsl:for-each>
                    </legalStatuses>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:apply-templates select="legalStatuses" mode="copy"/>
                </xsl:otherwise>
            </xsl:choose>
            <xsl:choose>
                <xsl:when test="count(function) &gt; 0">
                    <functions>
                        <xsl:for-each select="function">
                            <xsl:apply-templates select="." mode="copy"/>
                        </xsl:for-each>
                    </functions>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:apply-templates select="functions" mode="copy"/>
                </xsl:otherwise>
            </xsl:choose>
            <xsl:choose>
                <xsl:when test="count(occupation) &gt; 0">
                    <occupations>
                        <xsl:for-each select="occupation">
                            <xsl:apply-templates select="." mode="copy"/>
                        </xsl:for-each>
                    </occupations>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:apply-templates select="occupations" mode="copy"/>
                </xsl:otherwise>
            </xsl:choose>
            <xsl:choose>
                <xsl:when test="count(mandate) &gt; 0">
                    <mandates>
                        <xsl:for-each select="mandate">
                            <xsl:apply-templates select="." mode="copy"/>
                        </xsl:for-each>
                    </mandates>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:apply-templates select="mandates" mode="copy"/>
                </xsl:otherwise>
            </xsl:choose>
            <xsl:choose>
                <xsl:when test="count(languageUsed) &gt; 0">
                    <languagesUsed>
                        <xsl:for-each select="languageUsed">
                            <xsl:apply-templates select="." mode="copy"/>
                        </xsl:for-each>
                    </languagesUsed>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:apply-templates select="languagesUsed" mode="copy"/>
                </xsl:otherwise>
            </xsl:choose>
            <xsl:apply-templates select="structureOrGenealogy" mode="copy"/>
            <xsl:apply-templates select="generalContext" mode="copy"/>
            <xsl:apply-templates select="biogHist" mode="copy"/>
        </description>
    </xsl:template>

    <!-- existDates -->
    <xsl:template match="existDates" mode="copy">
        <existDates>
            <xsl:choose>
                <xsl:when test="count(date) = 0 and count(dateRange) = 0">
                    <dateRange>
                        <fromDate>
                            <xsl:attribute name="standardDate">
                                <xsl:text>0001</xsl:text>
                            </xsl:attribute>
                        </fromDate>
                        <toDate>
                            <xsl:attribute name="standardDate">
                                <xsl:text>2099</xsl:text>
                            </xsl:attribute>
                        </toDate>
                    </dateRange>
                </xsl:when>
                <xsl:when test="count(date) = 1 and count(dateRange) = 0">
                    <xsl:apply-templates select="date" mode="copy"/>
                </xsl:when>
                <xsl:when test="count(dateRange) = 1 and count(date) = 0">
                    <xsl:apply-templates select="dateRange" mode="copy"/>
                </xsl:when>
                <xsl:otherwise>
                    <dateSet>
                        <xsl:for-each select="date | dateRange | dateSet/date | dateSet/dateRange">
                            <xsl:apply-templates select="." mode="copy"/>
                        </xsl:for-each>
                    </dateSet>
                </xsl:otherwise>
            </xsl:choose>
            <xsl:apply-templates select="descriptiveNote" mode="copy"/>
        </existDates>
    </xsl:template>

    <!-- places -->
    <xsl:template match="places" mode="copy">
        <xsl:if test="*[text()]">
            <places>
                <xsl:apply-templates select="node()" mode="copy"/>
            </places>
        </xsl:if>
    </xsl:template>

    <!-- place -->
    <xsl:template match="place" mode="copy">
        <place>
            <xsl:if test="not(placeEntry)">
                <placeEntry/>
            </xsl:if>
            <xsl:apply-templates select="node()" mode="copy"/>
        </place>
    </xsl:template>

    <!-- placeRole -->
    <xsl:template match="placeRole" mode="copy">
        <placeRole>
            <xsl:if test="@lastDateTimeVerified">
                <xsl:attribute name="lastDateTimeVerified" select="@lastDateTimeVerified"/>
            </xsl:if>
            <xsl:if test="@scriptCode">
                <xsl:attribute name="scriptCode" select="@scriptCode"/>
            </xsl:if>
            <xsl:if test="@transliteration">
                <xsl:attribute name="transliteration" select="@transliteration"/>
            </xsl:if>
            <xsl:if test="@vocabularySource">
                <xsl:attribute name="vocabularySource" select="@vocabularySource"/>
            </xsl:if>
            <xsl:if test="@xml:lang">
                <xsl:attribute name="xml:lang" select="@xml:lang"/>
            </xsl:if>
            <xsl:value-of select="."/>
        </placeRole>
    </xsl:template>

    <!-- address -->
    <xsl:template match="address" mode="copy">
        <address>
            <xsl:if test="@localType">
                <xsl:attribute name="localType" select="@localType"/>
            </xsl:if>
            <xsl:for-each select="addressLine">
                <addressLine>
                    <xsl:if test="@xml:lang">
                        <xsl:attribute name="xml:lang" select="@xml:lang"/>
                    </xsl:if>
                    <xsl:if test="@localType">
                        <xsl:attribute name="localType" select="@localType"/>
                    </xsl:if>
                    <xsl:value-of select="."/>
                </addressLine>
            </xsl:for-each>
        </address>
    </xsl:template>

    <!-- localDescriptions -->
    <xsl:template match="localDescriptions" mode="copy">
        <localDescriptions>
            <xsl:attribute name="localType" select="@localType"/>
            <xsl:apply-templates select="node()" mode="copy"/>
        </localDescriptions>
    </xsl:template>

    <!-- localDescription -->
    <xsl:template match="localDescription" mode="copy">
        <localDescription>
            <xsl:attribute name="localType" select="@localType"/>
            <xsl:apply-templates select="node()" mode="copy"/>
        </localDescription>
    </xsl:template>

    <!-- legalStatuses -->
    <xsl:template match="legalStatuses" mode="copy">
        <legalStatuses>
            <xsl:apply-templates select="node()" mode="copy"/>
        </legalStatuses>
    </xsl:template>

    <!-- legalStatus -->
    <xsl:template match="legalStatus" mode="copy">
        <legalStatus>
            <xsl:if test="@localType">
                <xsl:attribute name="localType" select="@localType"/>
            </xsl:if>
            <xsl:apply-templates select="node()" mode="copy"/>
        </legalStatus>
    </xsl:template>

    <!-- functions -->
    <xsl:template match="functions" mode="copy">
        <xsl:if test="*[text()]">
            <functions>
                <xsl:apply-templates select="node()" mode="copy"/>
            </functions>
        </xsl:if>
    </xsl:template>

    <!-- function -->
    <xsl:template match="function" mode="copy">
        <function>
            <xsl:if test="@localType">
                <xsl:attribute name="localType" select="@localType"/>
            </xsl:if>
            <xsl:apply-templates select="node()" mode="copy"/>
        </function>
    </xsl:template>

    <!-- occupations -->
    <xsl:template match="occupations" mode="copy">
        <xsl:if test="./*[text()]">
            <occupations>
                <xsl:apply-templates select="node()" mode="copy"/>
            </occupations>
        </xsl:if>
    </xsl:template>

    <!-- occupation -->
    <xsl:template match="occupation" mode="copy">
        <occupation>
            <xsl:if test="@localType">
                <xsl:attribute name="localType" select="@localType"/>
            </xsl:if>
            <xsl:apply-templates select="node()" mode="copy"/>
        </occupation>
    </xsl:template>

    <!-- mandates -->
    <xsl:template match="mandates" mode="copy">
        <xsl:if test="./*[text()]">
            <mandates>
                <xsl:apply-templates select="node()" mode="copy"/>
            </mandates>
        </xsl:if>
    </xsl:template>

    <!-- mandate -->
    <xsl:template match="mandate" mode="copy">
        <mandate>
            <xsl:if test="@localType">
                <xsl:attribute name="localType" select="@localType"/>
            </xsl:if>
            <xsl:apply-templates select="node()" mode="copy"/>
        </mandate>
    </xsl:template>

    <!-- languagesUsed -->
    <xsl:template match="languagesUsed" mode="copy">
        <xsl:if test="./*[text()]">
            <languagesUsed>
                <xsl:apply-templates select="node()" mode="copy"/>
            </languagesUsed>
        </xsl:if>
    </xsl:template>

    <!-- languageUsed -->
    <xsl:template match="languageUsed" mode="copy">
        <languageUsed>
            <xsl:if test="@localType">
                <xsl:attribute name="localType" select="@localType"/>
            </xsl:if>
            <xsl:apply-templates select="node()" mode="copy"/>
        </languageUsed>
    </xsl:template>

    <!-- structureOrGenealogy -->
    <xsl:template match="structureOrGenealogy" mode="copy">
        <xsl:if test="*[text()]">
            <structureOrGenealogy>
                <xsl:apply-templates select="node()" mode="copy"/>
            </structureOrGenealogy>
        </xsl:if>
    </xsl:template>

    <!-- outline -->
    <xsl:template match="outline" mode="copy">
        <outline>
            <xsl:apply-templates select="node()" mode="copy"/>
        </outline>
    </xsl:template>

    <!-- level -->
    <xsl:template match="level" mode="copy">
        <level>
            <xsl:if test="@localType">
                <xsl:attribute name="localType" select="@localType"/>
            </xsl:if>
            <xsl:if test="@xml:lang">
                <xsl:attribute name="xml:lang" select="@xml:lang"/>
            </xsl:if>
            <xsl:apply-templates select="node()" mode="copy"/>
        </level>
    </xsl:template>

    <!-- item -->
    <xsl:template match="item" mode="copy">
        <item>
            <xsl:if test="@localType">
                <xsl:attribute name="localType" select="@localType"/>
            </xsl:if>
            <xsl:if test="@xml:lang">
                <xsl:attribute name="xml:lang" select="@xml:lang"/>
            </xsl:if>
            <xsl:apply-templates select="node()" mode="copy"/>
        </item>
    </xsl:template>

    <!-- p -->
    <xsl:template match="p" mode="copy">
        <!--<xsl:choose>
            <xsl:when
                test="parent::functions|languagesUsed|legalStatuses|localDescriptions|mandates|occupations|places">
                <xsl:variable name="thisElement" select="."/>
                <xsl:variable name="singularName">
                    <xsl:choose>
                        <xsl:when test="parent::languagesUsed">
                            <xsl:value-of>languageUsed</xsl:value-of>
                        </xsl:when>
                        <xsl:when test="parent::legalStatuses">
                            <xsl:value-of>legalStatus</xsl:value-of>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:value-of
                                select="substring(name(..), 0, string-length(name(..)) - 1)"/>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:variable>
                <xsl:for-each select="../$singularName">
                    <xsl:choose>
                        <xsl:when test="descriptiveNote">
                            
                        </xsl:when>
                        <xsl:otherwise>
                            <descriptiveNote>
                                <p>
                                    <xsl:if test="@xml:lang">
                                        <xsl:attribute name="xml:lang" select="$thisElement/@xml:lang"/>
                                    </xsl:if>
                                    <xsl:value-of select="$thisElement"/>
                                </p>
                            </descriptiveNote>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:for-each>
            </xsl:when>
            <xsl:otherwise>-->
                <p>
                    <xsl:if test="@xml:lang">
                        <xsl:attribute name="xml:lang" select="@xml:lang"/>
                    </xsl:if>
                    <xsl:value-of select="."/>
                </p><!--
            </xsl:otherwise>
        </xsl:choose>-->
    </xsl:template>

    <!-- generalContext -->
    <xsl:template match="generalContext" mode="copy">
        <xsl:if test="*[text()]">
            <generalContext>
                <xsl:apply-templates select="node()" mode="copy"/>
            </generalContext>
        </xsl:if>
    </xsl:template>

    <!-- biogHist -->
    <xsl:template match="biogHist" mode="copy">
        <xsl:if test="*[text()]">
            <biogHist>
                <xsl:if test="@localType">
                    <xsl:attribute name="localType" select="@localType"/>
                </xsl:if>
                <xsl:apply-templates select="node()" mode="copy"/>
            </biogHist>
        </xsl:if>
    </xsl:template>

    <!-- abstract -->
    <xsl:template match="abstract" mode="copy">
        <abstract>
            <xsl:if test="@xml:lang">
                <xsl:attribute name="xml:lang" select="@xml:lang"/>
            </xsl:if>
            <xsl:apply-templates select="node()" mode="copy"/>
        </abstract>
    </xsl:template>

    <!-- chronList -->
    <xsl:template match="chronList" mode="copy">
        <chronList>
            <xsl:for-each select="chronItem">
                <chronItem>
                    <xsl:apply-templates select="node()" mode="copy"/>
                </chronItem>
            </xsl:for-each>
        </chronList>
    </xsl:template>

    <!-- event -->
    <xsl:template match="event" mode="copy">
        <event>
            <xsl:if test="@localType">
                <xsl:attribute name="localType" select="@localType"/>
            </xsl:if>
            <xsl:if test="@xml:lang">
                <xsl:attribute name="xml:lang" select="@xml:lang"/>
            </xsl:if>
            <xsl:apply-templates select="node()" mode="copy"/>
        </event>
    </xsl:template>

    <!-- relations -->
    <xsl:template match="relations" mode="copy">
        <relations>
            <xsl:apply-templates select="node()" mode="copy"/>
        </relations>
    </xsl:template>

    <!-- cpfRelation -->
    <xsl:template match="cpfRelation" mode="copy">
        <cpfRelation>
            <xsl:if test="@cpfRelationType">
                <xsl:attribute name="cpfRelationType" select="@cpfRelationType"/>
            </xsl:if>
            <xsl:if test="@lastDateTimeVerified">
                <xsl:attribute name="lastDateTimeVerified" select="@lastDateTimeVerified"/>
            </xsl:if>
            <xsl:if test="@xml:lang">
                <xsl:attribute name="xml:lang" select="@xml:lang"/>
            </xsl:if>
            <xsl:attribute name="xlink:type" select="'simple'"/>
            <xsl:if test="@xlink:href">
                <xsl:attribute name="xlink:href" select="@xlink:href"/>
            </xsl:if>
            <xsl:if test="@xlink:arcrole">
                <xsl:attribute name="xlink:arcrole" select="@xlink:arcrole"/>
            </xsl:if>
            <xsl:if test="@xlink:role">
                <xsl:attribute name="xlink:role" select="@xlink:role"/>
            </xsl:if>
            <xsl:if test="@xlink:title">
                <xsl:attribute name="xlink:title" select="@xlink:title"/>
            </xsl:if>
            <xsl:apply-templates select="relationEntry" mode="copy"/>
            <xsl:if test="count(date) > 0 or count(dateRange) > 0 or count(dateSet) > 0">
                <xsl:choose>
                    <xsl:when test="count(date) = 1 and count(dateRange) = 0">
                        <xsl:apply-templates select="date" mode="copy"/>
                    </xsl:when>
                    <xsl:when test="count(dateRange) = 1 and count(date) = 0">
                        <xsl:apply-templates select="dateRange" mode="copy"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <dateSet>
                            <xsl:for-each
                                select="date | dateRange | dateSet/date | dateSet/dateRange">
                                <xsl:apply-templates select="." mode="copy"/>
                            </xsl:for-each>
                        </dateSet>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:if>
            <xsl:apply-templates select="placeEntry" mode="copy"/>
            <xsl:apply-templates select="descriptiveNote" mode="copy"/>
        </cpfRelation>
    </xsl:template>

    <!-- functionRelation -->
    <xsl:template match="functionRelation" mode="copy">
        <functionRelation>
            <xsl:if test="@functionRelationType">
                <xsl:attribute name="functionRelationType" select="@functionRelationType"/>
            </xsl:if>
            <xsl:if test="@lastDateTimeVerified">
                <xsl:attribute name="lastDateTimeVerified" select="@lastDateTimeVerified"/>
            </xsl:if>
            <xsl:if test="@xml:lang">
                <xsl:attribute name="xml:lang" select="@xml:lang"/>
            </xsl:if>
            <xsl:attribute name="xlink:type" select="'simple'"/>
            <xsl:if test="@xlink:href">
                <xsl:attribute name="xlink:href" select="@xlink:href"/>
            </xsl:if>
            <xsl:if test="@xlink:arcrole">
                <xsl:attribute name="xlink:arcrole" select="@xlink:arcrole"/>
            </xsl:if>
            <xsl:if test="@xlink:role">
                <xsl:attribute name="xlink:role" select="@xlink:role"/>
            </xsl:if>
            <xsl:if test="@xlink:title">
                <xsl:attribute name="xlink:title" select="@xlink:title"/>
            </xsl:if>
            <xsl:apply-templates select="relationEntry" mode="copy"/>
            <xsl:if test="count(date) > 0 or count(dateRange) > 0 or count(dateSet) > 0">
                <xsl:choose>
                    <xsl:when test="count(date) = 1 and count(dateRange) = 0">
                        <xsl:apply-templates select="date" mode="copy"/>
                    </xsl:when>
                    <xsl:when test="count(dateRange) = 1 and count(date) = 0">
                        <xsl:apply-templates select="dateRange" mode="copy"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <dateSet>
                            <xsl:for-each
                                select="date | dateRange | dateSet/date | dateSet/dateRange">
                                <xsl:apply-templates select="." mode="copy"/>
                            </xsl:for-each>
                        </dateSet>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:if>
            <xsl:apply-templates select="placeEntry" mode="copy"/>
            <xsl:apply-templates select="descriptiveNote" mode="copy"/>
        </functionRelation>
    </xsl:template>

    <!-- resourceRelation -->
    <xsl:template match="resourceRelation" mode="copy">
        <resourceRelation>
            <xsl:if test="@resourceRelationType">
                <xsl:attribute name="resourceRelationType" select="@resourceRelationType"/>
            </xsl:if>
            <xsl:if test="@lastDateTimeVerified">
                <xsl:attribute name="lastDateTimeVerified" select="@lastDateTimeVerified"/>
            </xsl:if>
            <xsl:if test="@xml:lang">
                <xsl:attribute name="xml:lang" select="@xml:lang"/>
            </xsl:if>
            <xsl:attribute name="xlink:type" select="'simple'"/>
            <xsl:if test="@xlink:href">
                <xsl:attribute name="xlink:href" select="@xlink:href"/>
            </xsl:if>
            <xsl:if test="@xlink:arcrole">
                <xsl:attribute name="xlink:arcrole" select="@xlink:arcrole"/>
            </xsl:if>
            <xsl:if test="@xlink:role">
                <xsl:attribute name="xlink:role" select="@xlink:role"/>
            </xsl:if>
            <xsl:if test="@xlink:title">
                <xsl:attribute name="xlink:title" select="@xlink:title"/>
            </xsl:if>
            <xsl:apply-templates select="relationEntry" mode="copy"/>
            <xsl:if test="count(date) > 0 or count(dateRange) > 0 or count(dateSet) > 0">
                <xsl:choose>
                    <xsl:when test="count(date) = 1 and count(dateRange) = 0">
                        <xsl:apply-templates select="date" mode="copy"/>
                    </xsl:when>
                    <xsl:when test="count(dateRange) = 1 and count(date) = 0">
                        <xsl:apply-templates select="dateRange" mode="copy"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <dateSet>
                            <xsl:for-each
                                select="date | dateRange | dateSet/date | dateSet/dateRange">
                                <xsl:apply-templates select="." mode="copy"/>
                            </xsl:for-each>
                        </dateSet>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:if>
            <xsl:apply-templates select="placeEntry" mode="copy"/>
            <xsl:apply-templates select="descriptiveNote" mode="copy"/>
        </resourceRelation>
    </xsl:template>

    <!-- relationEntry -->
    <xsl:template match="relationEntry" mode="copy">
        <relationEntry>
            <xsl:if test="@localType">
                <xsl:attribute name="localType" select="@localType"/>
            </xsl:if>
            <xsl:if test="@scriptCode">
                <xsl:attribute name="scriptCode" select="@scriptCode"/>
            </xsl:if>
            <xsl:if test="@transliteration">
                <xsl:attribute name="transliteration" select="@transliteration"/>
            </xsl:if>
            <xsl:if test="@xml:lang">
                <xsl:attribute name="xml:lang" select="@xml:lang"/>
            </xsl:if>
            <xsl:value-of select="."/>
        </relationEntry>
    </xsl:template>

    <!-- alternativeSet -->
    <xsl:template match="alternativeSet" mode="copy">
        <alternativeSet>
            <xsl:if test="@xml:lang">
                <xsl:attribute name="xml:lang" select="@xml:lang"/>
            </xsl:if>
            <xsl:if test="@xml:base">
                <xsl:attribute name="xml:base" select="@xml:base"/>
            </xsl:if>
            <xsl:apply-templates select="node()" mode="copy"/>
        </alternativeSet>
    </xsl:template>

    <!-- setComponent -->
    <xsl:template match="setComponent" mode="copy">
        <setComponent>
            <xsl:if test="@lastDateTimeVerified">
                <xsl:attribute name="lastDateTimeVerified" select="@lastDateTimeVerified"/>
            </xsl:if>
            <xsl:if test="@xml:lang">
                <xsl:attribute name="xml:lang" select="@xml:lang"/>
            </xsl:if>
            <xsl:attribute name="xlink:type" select="'simple'"/>
            <xsl:if test="@xlink:href">
                <xsl:attribute name="xlink:href" select="@xlink:href"/>
            </xsl:if>
            <xsl:if test="@xlink:role">
                <xsl:attribute name="xlink:role" select="@xlink:role"/>
            </xsl:if>
            <xsl:if test="@xlink:title">
                <xsl:attribute name="xlink:title" select="@xlink:title"/>
            </xsl:if>
            <xsl:apply-templates select="node()" mode="copy"/>
        </setComponent>
    </xsl:template>

    <!-- componentEntry -->
    <xsl:template match="componentEntry" mode="copy">
        <componentEntry>
            <xsl:if test="@localType">
                <xsl:attribute name="localType" select="@localType"/>
            </xsl:if>
            <xsl:if test="@scriptCode">
                <xsl:attribute name="scriptCode" select="@scriptCode"/>
            </xsl:if>
            <xsl:if test="@transliteration">
                <xsl:attribute name="transliteration" select="@transliteration"/>
            </xsl:if>
            <xsl:if test="@xml:lang">
                <xsl:attribute name="xml:lang" select="@xml:lang"/>
            </xsl:if>
            <xsl:value-of select="."/>
        </componentEntry>
    </xsl:template>

    <!-- Ignore structureOrGenealogy/citation, objectXMLWrap, objectBinWrap - they are not used -->
    <xsl:template match="citation[parent::structureOrGenealogy] | objectXMLWrap | objectBinWrap"
        mode="#all">
        <xsl:call-template name="excludeElement"/>
    </xsl:template>

    <!-- Ignore any relation elements with a object___Wrap element as only child -->
    <xsl:template
        match="cpfRelation[count(child::*) = 1 and (child::objectXMLWrap or child::objectBinWrap)]"
        mode="copy">
        <xsl:call-template name="excludeElement"/>
    </xsl:template>
    <xsl:template
        match="resourceRelation[count(child::*) = 1 and (child::objectXMLWrap or child::objectBinWrap)]"
        mode="copy">
        <xsl:call-template name="excludeElement"/>
    </xsl:template>
    <xsl:template
        match="functionRelation[count(child::*) = 1 and (child::objectXMLWrap or child::objectBinWrap)]"
        mode="copy">
        <xsl:call-template name="excludeElement"/>
    </xsl:template>

    <!-- Exclude unknown elements, return message -->
    <xsl:template match="*" name="excludeElement" mode="#all">
        <xsl:if test="ape:flagSet() = 'true'">
            <xsl:message>Element(s) excluded from the transformation:</xsl:message>
        </xsl:if>
        <xsl:variable name="excludedElement">
            <xsl:if test="name(../../../../../../../..) != ''">
                <xsl:value-of select="name(../../../../../../../..)"/>
                <xsl:if test="name(../../../../../../../..)='c'">@<xsl:value-of
                        select="../../../../../../../../@level"/></xsl:if>
                <xsl:text>/</xsl:text>
            </xsl:if>
            <xsl:if test="name(../../../../../../..) != ''">
                <xsl:value-of select="name(../../../../../../..)"/>
                <xsl:if test="name(../../../../../../..)='c'">@<xsl:value-of
                        select="../../../../../../../@level"/></xsl:if>
                <xsl:text>/</xsl:text>
            </xsl:if>
            <xsl:if test="name(../../../../../..) != ''">
                <xsl:value-of select="name(../../../../../..)"/>
                <xsl:if test="name(../../../../../..)='c'">@<xsl:value-of
                        select="../../../../../../@level"/></xsl:if>
                <xsl:text>/</xsl:text>
            </xsl:if>
            <xsl:if test="name(../../../../..) != ''">
                <xsl:value-of select="name(../../../../..)"/>
                <xsl:if test="name(../../../../..)='c'">@<xsl:value-of
                        select="../../../../../@level"/></xsl:if>
                <xsl:text>/</xsl:text>
            </xsl:if>
            <xsl:if test="name(../../../..) != ''">
                <xsl:value-of select="name(../../../..)"/>
                <xsl:if test="name(../../../..)='c'">@<xsl:value-of select="../../../../@level"
                    /></xsl:if>
                <xsl:text>/</xsl:text>
            </xsl:if>
            <xsl:if test="name(../../..) != ''">
                <xsl:value-of select="name(../../..)"/>
                <xsl:if test="name(../../..)='c'">@<xsl:value-of select="../../../@level"/></xsl:if>
                <xsl:text>/</xsl:text>
            </xsl:if>
            <xsl:value-of select="name(../..)"/>
            <xsl:if test="name(../..)='c'">@<xsl:value-of select="../../@level"/></xsl:if>
            <xsl:text>/</xsl:text>
            <xsl:value-of select="name(..)"/>
            <xsl:if test="name(..)='c'">@<xsl:value-of select="../@level"/></xsl:if>
            <xsl:text>/</xsl:text>
            <xsl:value-of select="name(.)"/>
            <xsl:if test="name(.)='c'">@<xsl:value-of select="@level"/></xsl:if>
        </xsl:variable>
        <xsl:message select="normalize-space($excludedElement)"/>
    </xsl:template>
</xsl:stylesheet>
