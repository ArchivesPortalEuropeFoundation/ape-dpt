<?xml version="1.0" encoding="UTF-8"?>
<!--
  #%L
  Data Preparation Tool Standalone mapping tool
  %%
  Copyright (C) 2009 - 2014 Archives Portal Europe
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

<!--
	EAG: Changing the EAG namespace to comply with the schema
-->
<xsl:stylesheet version="2.0" xmlns="http://www.archivesportaleurope.net/Portal/profiles/eag_2012/"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format"
                xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                xpath-default-namespace="http://www.archivesportaleurope.net/Portal/profiles/eag_2012/"
                exclude-result-prefixes="xsl fo xs xsi">

	<xsl:output indent="yes" method="xml" />

    <xsl:param name="name" select="''"/>
    <xsl:param name="english_name" select="''"/>
    <xsl:param name="eagid" select="''"/>
    <xsl:param name="person_lastname" select="''"/>
    <xsl:param name="person_firstname" select="''"/>
    <xsl:param name="country" select="''"/>
    <xsl:param name="city" select="''"/>
    <xsl:param name="postalcode" select="''"/>
    <xsl:param name="street" select="''"/>
    <xsl:param name="telephone" select="''"/>
    <xsl:param name="email" select="''"/>
    <xsl:param name="webpage" select="''"/>
    <xsl:param name="repositorycode" select="''"/>
    <xsl:param name="countrycode" select="''"/>
    <xsl:param name="date_creation" select="''"/>

    <xsl:template match="node()">
        <xsl:element name="{local-name()}" namespace="http://www.archivesportaleurope.net/Portal/profiles/eag_2012/">
            <xsl:apply-templates select="node()|@*"/>
        </xsl:element>
    </xsl:template>

    <xsl:template match="text()|@*" priority="2">
        <xsl:copy>
            <xsl:apply-templates select="node()|@*"/>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="comment()" priority="3" />

    <!--Element that changes-->

    <xsl:template match="eagid">
        <eagid>
            <xsl:value-of select="$eagid"/>
        </eagid>
    </xsl:template>

    <xsl:template match="date">
        <date calendar="gregorian" era="ce">
            <xsl:attribute name="normal" select="$date_creation"/>
        </date>
    </xsl:template>
    
    <xsl:template match="repositorid">
        <repositorid>
            <xsl:attribute name="countrycode" select="$countrycode"/>
            <xsl:attribute name="repositorycode" select="$repositorycode"/>
        </repositorid>
    </xsl:template>

    <xsl:template match="surnames">
        <surnames>
            <xsl:value-of select="$person_lastname"/>
        </surnames>
    </xsl:template>

    <xsl:template match="firstname">
        <firstname>
            <xsl:value-of select="$person_firstname"/>
        </firstname>
    </xsl:template>

    <xsl:template match="autform">
        <autform>
            <xsl:value-of select="$name"/>
        </autform>
    </xsl:template>

    <xsl:template match="parform">
        <parform>
            <xsl:value-of select="$english_name"/>
        </parform>
    </xsl:template>

    <xsl:template match="country">
        <country>
            <xsl:value-of select="$country"/>
        </country>
    </xsl:template>

    <xsl:template match="municipality">
        <municipality>
            <xsl:value-of select="$city"/>
        </municipality>
    </xsl:template>

    <xsl:template match="street">
        <street>
            <xsl:value-of select="$street"/>
        </street>
    </xsl:template>

    <xsl:template match="postalcode">
        <postalcode>
            <xsl:value-of select="$postalcode"/>
        </postalcode>
    </xsl:template>

    <xsl:template match="telephone">
        <telephone>
            <xsl:value-of select="$telephone"/>
        </telephone>
    </xsl:template>

    <xsl:template match="email">
        <email>
            <xsl:attribute name="href">
                <xsl:value-of select="$email"/>
            </xsl:attribute>
            <xsl:value-of select="."/><xsl:value-of select="$email"/>
        </email>
    </xsl:template>

    <xsl:template match="webpage">
        <webpage>
            <xsl:attribute name="href">
                <xsl:value-of select="$webpage"/>
            </xsl:attribute>
            <xsl:value-of select="."/>
        </webpage>
    </xsl:template>

</xsl:stylesheet>
