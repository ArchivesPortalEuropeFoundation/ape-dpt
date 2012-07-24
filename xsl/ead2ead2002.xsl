<?xml version="1.0" encoding="UTF-8"?>
<!--
	EAD default convertion into APE-EAD
	Modes:
	    - lowest: file and item levels
	    - intermediate: series and subseries levels
	    - fonds: fonds level
	    - copy: top elements + archdesc until the first c@level=fonds (not included)
	    - other modes: specific for special purposes
-->
<xsl:stylesheet version="2.0" xmlns="urn:isbn:1-931666-22-9"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format"
                xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                xmlns:xlink="http://www.w3.org/1999/xlink"
                xsi:schemaLocation="urn:isbn:1-931666-22-9 http://www.loc.gov/ead/ead.xsd"
                xpath-default-namespace="urn:isbn:1-931666-22-9"
                exclude-result-prefixes="xsl fo xs">

    <xsl:output indent="yes" method="xml" />
    <xsl:strip-space elements="*" />

    <xsl:param name="countrycode" select="'FR'"/>
    <xsl:param name="mainagencycode" select="'FR-SIAF'"/>

    <xsl:template match="/">
        <xsl:apply-templates />
    </xsl:template>

    <xsl:template name="excludeElement">
        <xsl:variable name="excludedElement">
            <xsl:value-of select="name(../..)"/><xsl:text>/</xsl:text><xsl:value-of select="name(..)"/><xsl:text>/</xsl:text><xsl:value-of select="name(.)"/>
        </xsl:variable>
        <xsl:message select="normalize-space($excludedElement)" />
    </xsl:template>

    <xsl:template match="comment()" priority="3" />

    <xsl:template match="node()">
        <xsl:element name="{local-name()}" namespace="urn:isbn:1-931666-22-9">
            <xsl:apply-templates select="node()|@*"/>
        </xsl:element>
    </xsl:template>

    <xsl:template match="text()|@*" priority="2">
        <xsl:copy>
            <xsl:apply-templates select="node()|@*"/>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="*:ref" priority="2">
        <xsl:apply-templates />
    </xsl:template>

    <xsl:template match="*:ead">
        <ead xmlns="urn:isbn:1-931666-22-9"
             xmlns:xlink="http://www.w3.org/1999/xlink"
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="urn:isbn:1-931666-22-9 http://www.archivesportaleurope.eu/profiles/APEnet_EAD.xsd http://www.w3.org/1999/xlink http://www.loc.gov/standards/xlink/xlink.xsd" audience="external">
            <xsl:apply-templates select="node()" />
        </ead>
    </xsl:template>

    <xsl:template match="*:eadheader">
        <eadheader>
            <xsl:apply-templates select="node()" />
            <xsl:if test="not(*:filedesc)">
                <filedesc><titlestmt><titleproper/></titlestmt></filedesc>
            </xsl:if>
        </eadheader>
    </xsl:template>

    <xsl:template match="*:eadid">
        <eadid>
            <xsl:variable name="identifier">
                <xsl:value-of select="text()" />
            </xsl:variable>
            <xsl:attribute name="countrycode" select="$countrycode"/>
            <xsl:attribute name="mainagencycode" select="$mainagencycode"/>
            <xsl:attribute name="identifier" select="concat($mainagencycode, concat('-', $identifier))" />
            <xsl:value-of select="$identifier" />
        </eadid>
    </xsl:template>

    <xsl:template match="*:archdesc">
        <archdesc level="fonds">
            <xsl:apply-templates />
        </archdesc>
    </xsl:template>

    <xsl:template match="*:repository">
        <repository>
            <xsl:choose>
                <xsl:when test="normalize-space(text()) eq 'AD001'">Archives départementales de l'Ain<extref xlink:href="http://www.archives.ain.fr"/></xsl:when>
                <xsl:when test="normalize-space(text()) eq 'AD002' or normalize-space(text()) eq 'FR AD002'">Archives départementales de l'Aisne<extref xlink:href="http://www.archives.aisne.fr"/></xsl:when>
                <xsl:when test="normalize-space(text()) eq 'AD003'">Archives départementales de l'Allier<extref xlink:href="http://archives.allier.fr"/></xsl:when>
                <xsl:when test="normalize-space(text()) eq 'AD004' or normalize-space(text()) eq 'FR AD004'">Archives départementales des Alpes-de-Haute-Provence<extref xlink:href="http://www.archives04.fr"/></xsl:when>
                <xsl:when test="normalize-space(text()) eq 'AD005'">Archives départementales des Hautes-Alpes<extref xlink:href="http://www.archives05.fr"/></xsl:when>
                <xsl:when test="normalize-space(text()) eq 'AD006'">Archives départementales des Alpes-Maritimes<extref xlink:href="http://www.cg06.fr/fr/decouvrir-les-am/decouverte-du-patrimoine/les-archives-departementales/les-archives-departementales"/></xsl:when>
                <xsl:when test="normalize-space(text()) eq 'AD007' or normalize-space(text()) eq 'FR AD007'">Archives départementales de l'Ardèche<extref xlink:href="http://www.ardeche.fr/Culture/archives-departementales1861"/></xsl:when>
                <xsl:when test="normalize-space(text()) eq 'AD008' or normalize-space(text()) eq 'FR AD008'">Archives départementales des Ardennes<extref xlink:href="http://archives.cg08.fr"/></xsl:when>
                <xsl:when test="normalize-space(text()) eq 'AD009'">Archives départementales de l'Ariège<extref xlink:href="http://www.cg09.fr/v2/detail_questions.asp?THEME=578"/></xsl:when>
                <xsl:when test="normalize-space(text()) eq 'AD010'">Archives départementales de l'Aube<extref xlink:href="http://www.archives-aube.com"/></xsl:when>
                <xsl:when test="normalize-space(text()) eq 'AD011' or normalize-space(text()) eq 'FR AD011'">Archives départementales de l'Aude<extref xlink:href="http://www.aude.fr/104-la-conservation-des-archives-departementales.htm"/></xsl:when>
                <xsl:when test="normalize-space(text()) eq 'AD012'">Archives départementales de l'Aveyron<extref xlink:href="http://www.cg12.fr/site/haut/menu_principal/missions/culture_et_loisirs/archives_departementales"/></xsl:when>
                <xsl:when test="normalize-space(text()) eq 'AD013' or normalize-space(text()) eq 'FR AD013'">Archives départementales des Bouches-du-Rhône<extref xlink:href="http://www.archives13.fr"/></xsl:when>
                <xsl:when test="normalize-space(text()) eq 'AD014' or normalize-space(text()) eq 'FR AD014'">Archives départementales du Calvados</xsl:when>
                <xsl:when test="normalize-space(text()) eq 'AD015'">Archives départementales du Cantal<extref xlink:href="http://archives.cantal.fr"/></xsl:when>
                <xsl:when test="normalize-space(text()) eq 'AD016'">Archives départementales de la Charente<extref xlink:href="http://www.cg16.fr/culture-patrimoine/les-archives-departementales/index.html"/></xsl:when>
                <xsl:when test="normalize-space(text()) eq 'AD017'">Archives départementales de la Charente-Maritime<extref xlink:href="http://charente-maritime.fr/CG17/jcms/c_6659/les-archives"/></xsl:when>
                <xsl:when test="normalize-space(text()) eq 'AD018'">Archives départementales du Cher<extref xlink:href="http://www.cg18.fr/Archives-departementales,512"/></xsl:when>
                <xsl:when test="normalize-space(text()) eq 'AD019'">Archives départementales de la Corrèze<extref xlink:href="http://www.archives.cg19.fr"/></xsl:when>
                <xsl:when test="normalize-space(text()) eq 'AD02A'">Archives départementales de la Corse-du-Sud<extref xlink:href="http://www.cg-corsedusud.fr/patrimoine-et-culture/archives-departementales"/></xsl:when>
                <xsl:when test="normalize-space(text()) eq 'AD02B'">Archives départementales de la Haute-Corse<extref xlink:href="http://www.cg2b.fr/cg2b/cgi-bin/pages/index.pl?lang=fr&amp;idarbo=619"/></xsl:when>
                <xsl:when test="normalize-space(text()) eq 'AD021'">Archives départementales de la Côte d'Or<extref xlink:href="http://www.archives.cotedor.fr"/></xsl:when>
                <xsl:when test="normalize-space(text()) eq 'AD022' or normalize-space(text()) eq 'FR AD022'">Archives départementales des Côtes-d'Armor<extref xlink:href="http://archives.cotesdarmor.fr"/></xsl:when>
                <xsl:when test="normalize-space(text()) eq 'AD023'">Archives départementales de la Creuse<extref xlink:href="http://www.creuse.fr/rubrique80.html"/></xsl:when>
                <xsl:when test="normalize-space(text()) eq 'AD024'">Archives départementales de la Dordogne<extref xlink:href="http://archives.cg24.fr"/></xsl:when>
                <xsl:when test="normalize-space(text()) eq 'AD025'">Archives départementales du Doubs<extref xlink:href="http://archives.doubs.fr"/></xsl:when>
                <xsl:when test="normalize-space(text()) eq 'AD026' or normalize-space(text()) eq 'FR AD026'">Archives départementales de la Drôme<extref xlink:href="http://archives.ladrome.fr"/></xsl:when>
                <xsl:when test="normalize-space(text()) eq 'AD027'">Archives départementales de l'Eure<extref xlink:href="http://archives.cg27.fr"/></xsl:when>
                <xsl:when test="normalize-space(text()) eq 'AD028'">Archives départementales d'Eure-et-Loir<extref xlink:href="http://www.archives28.fr"/></xsl:when>
                <xsl:when test="normalize-space(text()) eq 'AD029'">Archives départementales du Finistère<extref xlink:href="http://www.cg29.fr/Le-Conseil-general-et-vous/Culture-et-loisirs/Patrimoine-historique-et-culturel/Les-Archives-departementales"/></xsl:when>
                <xsl:when test="normalize-space(text()) eq 'AD030'">Archives départementales du Gard</xsl:when>
                <xsl:when test="normalize-space(text()) eq 'AD031'">Archives départementales de la Haute-Garonne<extref xlink:href="http://www.archives.cg31.fr"/></xsl:when>
                <xsl:when test="normalize-space(text()) eq 'AD032'">Archives départementales du Gers<extref xlink:href="http://www.archives32.fr"/></xsl:when>
                <xsl:when test="normalize-space(text()) eq 'AD033'">Archives départementales de la Gironde<extref xlink:href="http://archives.gironde.fr"/></xsl:when>
                <xsl:when test="normalize-space(text()) eq 'AD034'">Archives départementales de l'Hérault<extref xlink:href="http://archives.herault.fr"/></xsl:when>
                <xsl:when test="normalize-space(text()) eq 'AD035'">Archives départementales d'Ille-et-Vilaine<extref xlink:href="http://www.archives35.fr"/></xsl:when>
                <xsl:when test="normalize-space(text()) eq 'AD036'">Archives départementales de l'Indre<extref xlink:href="http://www.indre.fr/culture/les-archives-departementales"/></xsl:when>
                <xsl:when test="normalize-space(text()) eq 'AD037'">Archives départementales d'Indre-et-Loire<extref xlink:href="http://archives.cg37.fr"/></xsl:when>
                <xsl:when test="normalize-space(text()) eq 'AD038' or normalize-space(text()) eq 'FR AD038'">Archives départementales de l'Isère<extref xlink:href="http://www.archives-isere.fr"/></xsl:when>
                <xsl:when test="normalize-space(text()) eq 'AD039' or normalize-space(text()) eq 'FR AD039'">Archives départementales du Jura<extref xlink:href="http://www.cg39.fr/Sport-et-culture/Culture/Les-archives-departementales"/></xsl:when>
                <xsl:when test="normalize-space(text()) eq 'AD040' or normalize-space(text()) eq 'FR AD040'">Archives départementales des Landes<extref xlink:href="http://www.archives.landes.org"/></xsl:when>
                <xsl:when test="normalize-space(text()) eq 'AD041' or normalize-space(text()) eq 'FR AD041'">Archives départementales du Loir-et-Cher<extref xlink:href="http://www.cg41.fr/jahia/cg41/Accueil/cadre_de_vie/archives-departementales.html"/></xsl:when>
                <xsl:when test="normalize-space(text()) eq 'AD042' or normalize-space(text()) eq 'FR AD042'">Archives départementales de la Loire<extref xlink:href="http://www.loire-archives.fr"/></xsl:when>
                <xsl:when test="normalize-space(text()) eq 'AD043'">Archives départementales de la Haute-Loire<extref xlink:href="http://www.archives43.fr"/></xsl:when>
                <xsl:when test="normalize-space(text()) eq 'AD044'">Archives départementales de la Loire-Atlantique<extref xlink:href="http://www.loire-atlantique.fr/jcms/cg_7518/archives-departementales"/></xsl:when>
                <xsl:when test="normalize-space(text()) eq 'AD045'">Archives départementales du Loiret<extref xlink:href="http://www.loiret.com/les-archives-departementales-23205.htm?RH=ACCUEIL&amp;RF=1276852658863"/></xsl:when>
                <xsl:when test="normalize-space(text()) eq 'AD046'">Archives départementales du Lot<extref xlink:href="http://archives.lot.fr"/></xsl:when>
                <xsl:when test="normalize-space(text()) eq 'AD047'">Archives départementales du Lot-et-Garonne<extref xlink:href="http://www.cg47.org/archives/accueil.htm"/></xsl:when>
                <xsl:when test="normalize-space(text()) eq 'AD048'">Archives départementales de la Lozère<extref xlink:href="http://archives.lozere.fr"/></xsl:when>
                <xsl:when test="normalize-space(text()) eq 'AD049'">Archives départementales du Maine-et-Loire<extref xlink:href="http://www.archives49.fr/"/></xsl:when>
                <xsl:when test="normalize-space(text()) eq 'AD050'">Archives départementales de la Manche<extref xlink:href="http://archives.manche.fr"/></xsl:when>
                <xsl:when test="normalize-space(text()) eq 'AD051'">Archives départementales de la Marne<extref xlink:href="http://archives.marne.fr"/></xsl:when>
                <xsl:when test="normalize-space(text()) eq 'AD052'">Archives départementales de la Haute-Marne<extref xlink:href="http://archives.haute-marne.fr"/></xsl:when>
                <xsl:when test="normalize-space(text()) eq 'AD053'">Archives départementales de la Mayenne<extref xlink:href="http://archives.lamayenne.fr"/></xsl:when>
                <xsl:when test="normalize-space(text()) eq 'AD054'">Archives départementales de la Meurthe-et-Moselle<extref xlink:href="http://www.archives.cg54.fr"/></xsl:when>
                <xsl:when test="normalize-space(text()) eq 'AD055'">Archives départementales de la Meuse<extref xlink:href="http://www.meuse.fr/page.php?url=culture-et-tourisme/archives-departementales"/></xsl:when>
                <xsl:when test="normalize-space(text()) eq 'AD056'">Archives départementales du Morbihan<extref xlink:href="http://www.morbihan.fr/archives"/></xsl:when>
                <xsl:when test="normalize-space(text()) eq 'AD057'">Archives départementales de la Moselle<extref xlink:href="http://www.archives57.com"/></xsl:when>
                <xsl:when test="normalize-space(text()) eq 'AD058' or normalize-space(text()) eq 'FR AD 058' or normalize-space(text()) eq 'FR AD058'">Archives départementales de la Nièvre<extref xlink:href="http://www.cg58.fr/services-ouverts-au-public/archives-departementales/"/></xsl:when>
                <xsl:when test="normalize-space(text()) eq 'AD059'">Archives départementales du Nord<extref xlink:href="http://www.archivesdepartementales.cg59.fr"/></xsl:when>
                <xsl:when test="normalize-space(text()) eq 'AD060'">Archives départementales de l'Oise<extref xlink:href="http://www.archives.oise.fr"/></xsl:when>
                <xsl:when test="normalize-space(text()) eq 'AD061' or normalize-space(text()) eq 'FR AD061'">Archives départementales de l'Orne<extref xlink:href="http://www.orne.fr/en/culture-patrimoine/archives-departementales-orne"/></xsl:when>
                <xsl:when test="normalize-space(text()) eq 'AD062'">Archives départementales du Pas-de-Calais<extref xlink:href="http://www.archivespasdecalais.fr"/></xsl:when>
                <xsl:when test="normalize-space(text()) eq 'AD063'">Archives départementales du Puy-de-Dôme<extref xlink:href="http://www.archivesdepartementales.puydedome.com"/></xsl:when>
                <xsl:when test="normalize-space(text()) eq 'AD064' or normalize-space(text()) eq 'FR AD064'">Archives départementales des Pyrénées-Atlantiques<extref xlink:href="http://www.archives.cg64.fr"/></xsl:when>
                <xsl:when test="normalize-space(text()) eq 'AD065'">Archives départementales des Hautes-Pyrénées<extref xlink:href="http://www.cg65.fr/front.aspx?sectionId=116"/></xsl:when>
                <xsl:when test="normalize-space(text()) eq 'AD066'">Archives départementales des Pyrénées-Orientales<extref xlink:href="http://www.cg66.fr/culture/archives/index.html"/></xsl:when>
                <xsl:when test="normalize-space(text()) eq 'AD067'">Archives départementales du Bas-Rhin<extref xlink:href="http://archives.bas-rhin.fr"/></xsl:when>
                <xsl:when test="normalize-space(text()) eq 'AD068'">Archives départementales du Haut-Rhin<extref xlink:href="http://www.archives.cg68.fr"/></xsl:when>
                <xsl:when test="normalize-space(text()) eq 'AD069'">Archives départementales du Rhône<extref xlink:href="http://archives.rhone.fr"/></xsl:when>
                <xsl:when test="normalize-space(text()) eq 'AD070'">Archives départementales de la Haute-Saône<extref xlink:href="http://archives.cg70.fr"/></xsl:when>
                <xsl:when test="normalize-space(text()) eq 'AD071'">Archives départementales de Saône-et-Loire<extref xlink:href="http://www.archives71.fr"/></xsl:when>
                <xsl:when test="normalize-space(text()) eq 'AD072'">Archives départementales de la Sarthe<extref xlink:href="http://www.archives.sarthe.com"/></xsl:when>
                <xsl:when test="normalize-space(text()) eq 'AD073' or normalize-space(text()) eq 'FR AD073'">Archives départementales de la Savoie<extref xlink:href="http://www.savoie-archives.fr"/></xsl:when>
                <xsl:when test="normalize-space(text()) eq 'AD074'">Archives départementales de la Haute-Savoie<extref xlink:href="http://www.archives.cg74.fr"/></xsl:when>
                <xsl:when test="normalize-space(text()) eq 'AD075'">Archives départementales et communales de Paris<extref xlink:href="http://www.paris.fr/politiques/paris-d-hier-a-aujourd-hui/archives-de-paris/p149"/></xsl:when>
                <xsl:when test="normalize-space(text()) eq 'AD076'">Archives départementales de la Seine-Maritime<extref xlink:href="http://www.archivesdepartementales76.net"/></xsl:when>
                <xsl:when test="normalize-space(text()) eq 'AD077'">Archives départementales de la Seine-et-Marne<extref xlink:href="http://archives.seine-et-marne.fr"/></xsl:when>
                <xsl:when test="normalize-space(text()) eq 'AD078' or normalize-space(text()) eq 'FR AD078'">Archives départementales des Yvelines<extref xlink:href="http://www.archives.yvelines.fr"/></xsl:when>
                <xsl:when test="normalize-space(text()) eq 'AD079'">Archives départementales des Deux-Sèvres<extref xlink:href="http://archives.deux-sevres.com"/></xsl:when>
                <xsl:when test="normalize-space(text()) eq 'AD080'">Archives départementales de la Somme<extref xlink:href="http://www.somme.fr/100-pratique/archives-departementales.html"/></xsl:when>
                <xsl:when test="normalize-space(text()) eq 'AD081' or normalize-space(text()) eq 'FR AD081'">Archives départementales du Tarn<extref xlink:href="http://archives.tarn.fr"/></xsl:when>
                <xsl:when test="normalize-space(text()) eq 'AD082'">Archives départementales du Tarn-et-Garonne<extref xlink:href="http://www.archivesdepartementales.cg82.fr"/></xsl:when>
                <xsl:when test="normalize-space(text()) eq 'AD083'">Archives départementales du Var<extref xlink:href="http://www.archives.var.fr"/></xsl:when>
                <xsl:when test="normalize-space(text()) eq 'AD084'">Archives départementales de Vaucluse<extref xlink:href="http://archives.vaucluse.fr"/></xsl:when>
                <xsl:when test="normalize-space(text()) eq 'AD085'">Archives départementales de la Vendée<extref xlink:href="http://archives.vendee.fr"/></xsl:when>
                <xsl:when test="normalize-space(text()) eq 'AD086'">Archives départementales de la Vienne<extref xlink:href="http://www.archives-vienne.cg86.fr"/></xsl:when>
                <xsl:when test="normalize-space(text()) eq 'AD087'">Archives départementales de la Haute-Vienne<extref xlink:href="http://www.archives-hautevienne.com"/></xsl:when>
                <xsl:when test="normalize-space(text()) eq 'AD088'">Archives départementales des Vosges<extref xlink:href="http://www.vosges-archives.com"/></xsl:when>
                <xsl:when test="normalize-space(text()) eq 'AD089'">Archives départementales de l'Yonne<extref xlink:href="http://www.lyonne.com/Culture-et-Vie-Locale/Archives-departementales"/></xsl:when>
                <xsl:when test="normalize-space(text()) eq 'AD090'">Archives départementales du Territoire-de-Belfort<extref xlink:href="http://www.archives.cg90.fr"/></xsl:when>
                <xsl:when test="normalize-space(text()) eq 'AD091'">Archives départementales de l'Essonne<extref xlink:href="http://www.archives.essonne.fr"/></xsl:when>
                <xsl:when test="normalize-space(text()) eq 'AD092' or normalize-space(text()) eq 'FR AD092'">Archives départementales des Hauts-de-Seine<extref xlink:href="http://archives.hauts-de-seine.net"/></xsl:when>
                <xsl:when test="normalize-space(text()) eq 'AD093'">Archives départementales de la Seine-Saint-Denis<extref xlink:href="http://archives.seine-saint-denis.fr"/></xsl:when>
                <xsl:when test="normalize-space(text()) eq 'AD094' or normalize-space(text()) eq 'FR AD094'">Archives départementales du Val de Marne<extref xlink:href="http://archives.cg94.fr"/></xsl:when>
                <xsl:when test="normalize-space(text()) eq 'AD095'">Archives départementales du Val-d'Oise<extref xlink:href="http://www.valdoise.fr/60-archives-departementales.htm"/></xsl:when>
                <xsl:when test="normalize-space(text()) eq 'AD988'">Archives départementales de Nouvelle-Calédonie</xsl:when>

                <xsl:when test="normalize-space(text()) eq 'Archives municipales de Marseille'">Archives municipales de Marseille<extref xlink:href="http://archives.marseille.fr" /></xsl:when>
                <xsl:when test="normalize-space(text()) eq 'Archives nationales (Fontainebleau)'">Archives nationales (Fontainebleau)<extref xlink:href="http://www.archivesnationales.culture.gouv.fr/chan/index.html" /></xsl:when>
                <xsl:when test="normalize-space(text()) eq 'Archives nationales (Paris)'">Archives nationales (Paris)<extref xlink:href="http://www.archivesnationales.culture.gouv.fr/chan/index.html" /></xsl:when>
                <xsl:when test="normalize-space(text()) eq 'CAMT'">Archives nationales du monde du travail (ex CAMT)<extref xlink:href="http://www.archivesnationales.culture.gouv.fr/camt/" /></xsl:when>
                <xsl:when test="normalize-space(text()) eq 'Centre d’archives d’architecture du XXe siècle, Ifa/Cité de l’architecture et du patrimoine'">Archives de l'IFA (architecture)</xsl:when>
                <xsl:when test="normalize-space(text()) eq 'FR CAOM'">Archives nationales d'outre-mer (ex CAOM)<extref xlink:href="http://www.archivesnationales.culture.gouv.fr/anom/fr/" /></xsl:when>
                <xsl:when test="normalize-space(text()) eq 'Musée Picasso (Paris)'">Archives du musée Picasso</xsl:when>
                <xsl:when test="normalize-space(text()) eq 'Service des Archives de la Communauté d''Agglomération Pau-Pyrénées'">Archives de la Communauté d'agglomération Pau-Pyrénées<extref xlink:href="http://archives.agglo-pau.fr"/></xsl:when>

                <xsl:otherwise><xsl:call-template name="excludeElement"/><xsl:value-of select="text()"/></xsl:otherwise>
            </xsl:choose>
        </repository>
    </xsl:template>

    <xsl:template match="*:unitid">
        <unitid encodinganalog="3.1.1" type="call number">
            <xsl:apply-templates />
        </unitid>
    </xsl:template>

    <xsl:template match="*:unittitle">
        <unittitle encodinganalog="3.1.2">
            <xsl:apply-templates />
        </unittitle>
    </xsl:template>

    <xsl:template match="*:unittitle/*:unitdate">
        <xsl:apply-templates />
    </xsl:template>

    <xsl:template match="*:title">
        <xsl:apply-templates />
    </xsl:template>

    <xsl:template match="*:unitdate">
        <unitdate encodinganalog="3.1.3" era="ce" calendar="gregorian">
            <xsl:apply-templates />
        </unitdate>
    </xsl:template>

    <xsl:template match="*:extref">
        <extref>
            <xsl:if test="@href">
                <xsl:attribute name="xlink:href" select="@href"/>
            </xsl:if>
            <xsl:if test="@xlink:href">
                <xsl:attribute name="xlink:href" select="@xlink:href"/>
            </xsl:if>
            <xsl:apply-templates />
        </extref>
    </xsl:template>

    <xsl:template match="*:p">
        <p>
            <xsl:apply-templates select="text() | *[name()!='list' and name()!='table']"/>
        </p>
        <xsl:apply-templates select="*:list | *:table"/>
    </xsl:template>

    <xsl:template match="*:p/*:note">
        <xsl:apply-templates select="node()"/>
    </xsl:template>

    <xsl:template match="*:p/*:note/*:p">
        (<xsl:apply-templates select="node()"/>)
    </xsl:template>

    <xsl:template match="*:p/*:unitdate">
        <xsl:apply-templates />
    </xsl:template>

    <xsl:template match="*:list">
        <list>
            <xsl:choose>
                <xsl:when test="@type='marked' or @type='ordered'">
                    <xsl:attribute name="type" select="@type"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:attribute name="type" select="'ordered'"/>
                </xsl:otherwise>
            </xsl:choose>
            <xsl:apply-templates />
        </list>
    </xsl:template>

    <xsl:template match="*:item/*:lb" />

    <xsl:template match="*:table">
        <table>
            <xsl:apply-templates />
        </table>
    </xsl:template>

    <xsl:template match="*:tgroup">
        <tgroup>
            <xsl:if test="@cols">
                <xsl:attribute name="cols" select="@cols" />
            </xsl:if>
            <xsl:apply-templates />
        </tgroup>
    </xsl:template>
    <xsl:template match="*:row">
        <row>
            <xsl:apply-templates />
        </row>
    </xsl:template>
    <xsl:template match="*:colspec">
        <colspec>
            <xsl:apply-templates />
        </colspec>
    </xsl:template>
    <xsl:template match="*:entry">
        <entry>
            <xsl:apply-templates />
        </entry>
    </xsl:template>

    <xsl:template match="*:bibliography/*:p">
        <p>
            <xsl:apply-templates select="node() except *:bibref" />
            <xsl:for-each select="*:bibref">
                <xsl:apply-templates select="node()" />
            </xsl:for-each>
        </p>
    </xsl:template>

    <xsl:template match="*:bibliography/*:bibref" mode="copy fonds intermediate lowest nested">
        <xsl:choose>
            <xsl:when test="*:extref">
                <p>
                    <xsl:if test="@href">
                        <extref>
                            <xsl:if test="@href">
                                <xsl:attribute name="xlink:href" select="@href"/>
                            </xsl:if>
                            <xsl:if test="@title">
                                <xsl:attribute name="xlink:title" select="@title"/>
                            </xsl:if>
                            <xsl:apply-templates select="node()" mode="#current"/>
                        </extref>
                    </xsl:if>
                    <xsl:if test="not(@href)">
                        <xsl:apply-templates select="node()" mode="#current"/>
                    </xsl:if>
                </p>
            </xsl:when>
            <xsl:otherwise>

            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template match="*:corpname">
        <corpname>
            <xsl:apply-templates />
        </corpname>
    </xsl:template>

    <xsl:template match="*:geogname">
        <geogname>
            <xsl:apply-templates />
        </geogname>
    </xsl:template>

    <xsl:template match="*:date">
        <xsl:apply-templates />
    </xsl:template>
    
    <xsl:template match="*:admininfo">
        <xsl:apply-templates />
    </xsl:template>

    <xsl:template match="*:add">
        <xsl:apply-templates />
    </xsl:template>

    <xsl:template match="*:emph">
        <xsl:apply-templates />
    </xsl:template>

    <xsl:template match="*:dsc">
        <xsl:for-each select="following-sibling::*[name()!='c' and name()!='dsc']">
            <xsl:element name="{local-name()}" namespace="urn:isbn:1-931666-22-9">
                <xsl:apply-templates select="node()|@*"/>
            </xsl:element>
        </xsl:for-each>
        <xsl:choose>
            <xsl:when test="ancestor::*[name()='dsc'] or parent::*[name()='c']">
                <xsl:apply-templates />
            </xsl:when>
            <xsl:otherwise>
                <dsc type="othertype">
                    <xsl:apply-templates />
                    <xsl:for-each select="following-sibling::*[name()='c']">
                        <xsl:element name="{local-name()}" namespace="urn:isbn:1-931666-22-9">
                            <xsl:attribute name="level" select="'fonds'"/>
                            <xsl:apply-templates select="node()"/>
                        </xsl:element>
                    </xsl:for-each>
                </dsc>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template match="*:scopecontent">
        <xsl:choose>
            <xsl:when test="preceding-sibling::*[name()='dsc']"/>
            <xsl:otherwise>
                <scopecontent encodinganalog="summary">
                    <xsl:apply-templates />
                </scopecontent>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template match="*:controlaccess">
        <xsl:choose>
            <xsl:when test="preceding-sibling::*[name()='dsc']"/>
            <xsl:otherwise>
                <controlaccess>
                    <xsl:apply-templates />
                </controlaccess>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template match="*:c">
        <xsl:choose>
            <xsl:when test="parent::*[name()!='dsc' and name()!='c']"/>
            <xsl:otherwise>
                <c>
                    <xsl:choose>
                        <xsl:when test="@level='othertype'">
                            <xsl:attribute name="level" select="'fonds'"/>
                        </xsl:when>
                        <xsl:when test="not(@level) or @level='recordgrp' or @level='otherlevel'">
                            <xsl:attribute name="level" select="'series'"/>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:attribute name="level" select="@level"/>
                        </xsl:otherwise>
                    </xsl:choose>
                    <xsl:apply-templates />
                </c>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

</xsl:stylesheet>