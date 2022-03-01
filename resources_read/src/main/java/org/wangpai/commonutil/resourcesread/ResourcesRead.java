package org.wangpai.commonutil.resourcesread;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.wangpai.commonutil.tc.TcUtil;

public class ResourcesRead {
    /**
     * 通过路径读取图片文件
     *
     * @param path 以 resource 的路径为基准，不需要以 / 为开头
     * @since 2022-3-3
     */
    public static byte[] readImage2ByteArray(String path) throws IOException {
        return TcUtil.inputStream2byteArray(readImage2InputStream(path));
    }

    /**
     * 通过路径读取图片文件
     *
     * @param path 以 resource 的路径为基准，不需要以 / 为开头
     * @since 2021-11-24
     */
    public static InputStream readImage2InputStream(String path) {
        /**
         * 方法 getResourceAsStream 的路径是以资源目录 resources 为基准的，
         * 且不受模块的限制。这于 xxx.class 中 xxx 是哪个模块的哪个类无关
         */
        var imageStream = ResourcesRead.class.getClassLoader()
                .getResourceAsStream(path);
        if (imageStream == null) {
            System.out.println("--readImage 为 null--：" + path); // FIXME 日志
        }
        return imageStream;
    }

    /**
     * 返回的结果不会包含文件夹流
     *
     * path 不需要以斜杠开头。path 是以资源目录 resources 为基准的
     *
     * @since 2022-1-16
     */
    public static InputStream[] readAllResFiles(String path, ReadMode mode) throws IOException {
        var resolver = new PathMatchingResourcePatternResolver();

        var suffix = switch (mode) {
            case RECURSIVE -> "**";
            case ONE_LEVEL_SUFFIX_FILE_ONLY -> "*.*";
            case ONE_LEVEL -> "*";
        };

        var resources = resolver.getResources("classpath:" + path + "/" + suffix);
        var inputStreams = new ArrayList<InputStream>(resources.length);
        for (var resource : resources) {
            // 如果 resource 不是目录
            if (resource.getFile().isFile()) {
                inputStreams.add(resource.getInputStream());
            }
        }
        return inputStreams.toArray(InputStream[]::new); // List 转数组
    }
}
