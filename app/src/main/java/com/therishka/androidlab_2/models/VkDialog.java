package com.therishka.androidlab_2.models;

/**
 * @author Rishad Mustafaev.
 */

public class VkDialog {
    private VkMessage mVkMessage;
    private long in_read;
    private long out_read;

    public VkDialog(VkMessage vkMessage, long in_read, long out_read) {
        mVkMessage = vkMessage;
        this.in_read = in_read;
        this.out_read = out_read;
    }

    public VkDialog() {
    }

    public VkMessage getVkMessage() {
        return mVkMessage;
    }

    public void setVkMessage(VkMessage vkMessage) {
        mVkMessage = vkMessage;
    }

    public long getIn_read() {
        return in_read;
    }

    public void setIn_read(long in_read) {
        this.in_read = in_read;
    }

    public long getOut_read() {
        return out_read;
    }

    public void setOut_read(long out_read) {
        this.out_read = out_read;
    }
}
