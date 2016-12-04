package com.devotion.blue.web;

import com.devotion.blue.message.Actions;
import com.devotion.blue.message.MessageKit;
import com.devotion.blue.web.core.JPress;
import com.devotion.blue.web.core.JPressConfig;
import com.devotion.blue.web.ui.function.*;
import com.devotion.blue.web.ui.tag.*;

public class Config extends JPressConfig {

    public void onJPressStarted() {

        JPress.addTag(ContentsTag.TAG_NAME, new ContentsTag());
        JPress.addTag(ContentTag.TAG_NAME, new ContentTag());
        JPress.addTag(ModulesTag.TAG_NAME, new ModulesTag());
        JPress.addTag(TagsTag.TAG_NAME, new TagsTag());
        JPress.addTag(TaxonomyTag.TAG_NAME, new TaxonomyTag());
        JPress.addTag(TaxonomysTag.TAG_NAME, new TaxonomysTag());
        JPress.addTag(ArchivesTag.TAG_NAME, new ArchivesTag());
        JPress.addTag(UsersTag.TAG_NAME, new UsersTag());

        JPress.addFunction("TAXONOMY_BOX", new TaxonomyBox());
        JPress.addFunction("OPTION", new OptionValue());
        JPress.addFunction("OPTION_CHECKED", new OptionChecked());
        JPress.addFunction("OPTION_SELECTED", new OptionSelected());
        JPress.addFunction("METADATA_CHECKED", new MetadataChecked());
        JPress.addFunction("METADATA_SELECTED", new MetadataSelected());

        MessageKit.sendMessage(Actions.JPRESS_STARTED);

    }


}
