package com.devotion.blue.model.template;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import com.devotion.blue.utils.FileUtils;
import com.devotion.blue.utils.StringUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.jfinal.log.Log;

public class TemplateConfigParser extends DefaultHandler {

    private static final Log log = Log.getLog(TemplateConfigParser.class);
    final Template template;

    private TplModule cModule;
    private TplTaxonomyType cTaxonomy;
    private List<TplModule> modules;
    private List<TplTaxonomyType> cTaxonomys;
    private List<TplMetadata> cModuleMetadatas;
    private List<TplMetadata> cTaxonomyMetadatas;
    private List<Thumbnail> thumbnails;

    private boolean taxonomyStarted = false;

    private String value = null;

    public TemplateConfigParser() {
        template = new Template();
        modules = new ArrayList<>();
        thumbnails = new ArrayList<>();
    }

    public Template parser(File templateFolder) {
        File configFile = new File(templateFolder, "tpl_config.xml");
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            parser.parse(configFile, this);
        } catch (Exception e) {
            log.warn("ConfigParser parser exception", e);
        }

        File screenshotFile = new File(templateFolder, "tpl_screenshot.png");
        if (screenshotFile.exists()) {
            template.setScreenshot(FileUtils.removeRootPath(screenshotFile.getAbsolutePath()));
        }

        String path = FileUtils.removeRootPath(templateFolder.getAbsolutePath());
        template.setPath(path.replace("\\", "/"));

        return template;
    }

    @Override
    public void endDocument() throws SAXException {
        template.setModules(modules);
        template.setThumbnails(thumbnails);
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attrs) throws SAXException {

        if ("module".equalsIgnoreCase(qName)) {
            cModule = new TplModule();
            cModule.setAddTitle(attrs.getValue("add"));
            cModule.setName(attrs.getValue("name"));
            cModule.setListTitle(attrs.getValue("list"));
            cModule.setTitle(attrs.getValue("title"));
            cModule.setCommentTitle(attrs.getValue("comment"));
            cModule.setOrders(attrs.getValue("orders"));
            cModule.setIconClass(attrs.getValue("iconClass"));

            cTaxonomys = new ArrayList<>();
            cModuleMetadatas = new ArrayList<>();

        }

        if ("taxonomy".equalsIgnoreCase(qName)) {
            cTaxonomy = new TplTaxonomyType(cModule);

            cTaxonomy.setName(attrs.getValue("name"));
            cTaxonomy.setTitle(attrs.getValue("title"));
            cTaxonomy.setFormType(attrs.getValue("formType"));

            cTaxonomyMetadatas = new ArrayList<>();
            taxonomyStarted = true;
        }

        if ("metadata".equalsIgnoreCase(qName)) {
            TplMetadata meta = new TplMetadata();

            meta.setName(attrs.getValue("name"));
            meta.setTitle(attrs.getValue("title"));
            meta.setDescription(attrs.getValue("description"));
            meta.setPlaceholder(attrs.getValue("placeholder"));
            String dataType = attrs.getValue("placeholder");
            if (StringUtils.isNotBlank(dataType)) {
                meta.setDataType(dataType);
            }

            if (taxonomyStarted) {
                cTaxonomyMetadatas.add(meta);
            } else {
                cModuleMetadatas.add(meta);
            }

        }

        if ("thumbnail".equalsIgnoreCase(qName)) {
            Thumbnail tb = new Thumbnail();
            tb.setName(attrs.getValue("name"));
            tb.setSize(attrs.getValue("size"));
            thumbnails.add(tb);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        qName = qName.toLowerCase();
        switch (qName) {
            case "module":
                cModule.setTaxonomyTypes(cTaxonomys);
                cModule.setMetadatas(cModuleMetadatas);
                modules.add(cModule);
                break;
            case "taxonomy":
                cTaxonomy.setMetadatas(cTaxonomyMetadatas);
                cTaxonomys.add(cTaxonomy);
                taxonomyStarted = false;
                break;
            case "title":
                template.setTitle(value);
                break;
            case "id":
                template.setId(value);
                break;
            case "description":
                template.setDescription(value);
                break;
            case "author":
                template.setAuthor(value);
                break;
            case "authorWebsite":
                template.setAuthorWebsite(value);
                break;
            case "version":
                template.setVersion(value);
                break;
            case "renderType":
                template.setRenderType(value);
                break;
            case "versionCode":
                int versionCode = 0;
                try {
                    versionCode = Integer.parseInt(value.trim());
                } catch (Exception e) {
                    log.error("String convert to Integer error:" + e);
                }
                template.setVersionCode(versionCode);
                break;
            case "updateUrl":
                template.setUpdateUrl(value);
                break;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        value = new String(ch, start, length);
    }

}
