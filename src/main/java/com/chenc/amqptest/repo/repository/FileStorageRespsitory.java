package com.chenc.amqptest.repo.repository;


/**
 * 文件储存仓库
 */
public interface FileStorageRespsitory {

    /**
     * 检查文件储存是否可用
     * @return 可用性
     */
    public Boolean isAvailable();

    /**
     * 保存文件
     * @param file byte[] 数组
     * @param path 文件路径
     * @return 是否保存成功
     */
    public boolean saveFile(byte[] file, String path);

    /**
     * 删除文件
     * @param path 文件路径
     * @return 是否删除成功
     */
    public Boolean deleteFile(String path);

    /**
     * 获取可以直接访问的路径
     * @param path
     * @return http地址
     */
    public String getURL(String path);
    
}
