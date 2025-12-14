package com.brevetest.brevetest;

import com.brevetest.brevetest.breveService.ReadAllBreveFilesFS;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class BreveTestExtension implements BeforeAllCallback {

    private final ReadAllBreveFilesFS breveReader = new ReadAllBreveFilesFS();

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        breveReader.runBreve();
    }
}
