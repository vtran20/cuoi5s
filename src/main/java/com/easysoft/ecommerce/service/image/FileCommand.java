package com.easysoft.ecommerce.service.image;

import java.io.File;

public interface FileCommand {
    void setIn(File in);
    void setOut(File out);
    void execute() throws Exception;
}
