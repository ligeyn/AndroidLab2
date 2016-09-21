package com.therishka.androidlab_2;

/**
 * @author Rishad Mustafaev
 */

public interface VkRequestsCallback {

    void started();

    void progress();

    void error();

    void success();

}
