package com.bocloud.webssh.booter.pojo;

import java.io.IOException;
import java.io.OutputStream;

public interface SSHConnect {

    OutputStream getOutputStream() throws IOException;

}
