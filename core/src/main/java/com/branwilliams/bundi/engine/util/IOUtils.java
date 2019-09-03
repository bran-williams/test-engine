package com.branwilliams.bundi.engine.util;

import org.lwjgl.system.MemoryUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public enum IOUtils {
    INSTANCE;

    private static final Logger LOG = LoggerFactory.getLogger(IOUtils.class);

    private static final String NEWLINE = "\n";

    /**
     * Reads the contents of a file with the newline character (\n) as the delimiter.
     * @see IOUtils#readFile(File, String, String)
     * */
    public static String readFile(Path file, String defaultText) {
        return readFile(file.toFile(), defaultText, NEWLINE);
    }

    /**
     * Reads the contents of a file with the newline character (\n) as the delimiter.
     * @see IOUtils#readFile(File, String, String)
     * */
    public static String readFile(Path directory, String file, String defaultText) {
        return readFile(directory.resolve(file).toFile(), defaultText, NEWLINE);
    }

    /**
     * @see IOUtils#readFile(File, String, String)
     * */
    public static String readFile(Path directory, String file, String defaultText, String delimiter) {
        return readFile(directory.resolve(file).toFile(), defaultText, delimiter);
    }

    /**
     * Reads the contents of a file with the newline character (\n) as the delimiter.
     * @see IOUtils#readFile(File, String, String)
     * */
    public static String readFile(String filePath, String defaultText) {
        return readFile(new File(filePath), defaultText, NEWLINE);
    }

    /**
     *
     * @see IOUtils#readFile(File, String, String)
     * */
    public static String readFile(String filePath, String defaultText, String delimiter) {
        return readFile(new File(filePath), defaultText, delimiter);
    }

    /**
     * Reads the contents of a file with the newline character (\n) as the delimiter.
     * @see IOUtils#readFile(File, String, String)
     * */
    public static String readFile(File file, String defaultText) {
        return readFile(file, defaultText, NEWLINE);
    }

    /**
     * Reads the contents of a file, line by line, into a string. If the file cannot be read, a default string is
     * returned. The lines of the file are concatenated into one string, where each line is separated by a delimiter.
     *
     * @param file The {@link File} to read from.
     * @param defaultText The default String returned if the file could not be read.
     * @param delimiter The delimiter used to separate each line read from the file.
     * @return The contents of the file, as a string, with each line separated by some delimiter. If the file is unable
     * to be read, then some default text is returned.
     * */
    public static String readFile(File file, String defaultText, String delimiter) {

        if (file == null) {
            LOG.error("Unable to read null file");
            return defaultText;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            defaultText = reader.lines().collect(Collectors.joining(delimiter));
        } catch (IOException e) {
            LOG.error(String.format("Unable to read file '%s', error: %s", file, e.getMessage()));
        }

        return defaultText;
    }

    /**
     * Reads the specified resource and returns the raw data as a ByteBuffer.
     *
     * @author LWJGL
     * @author Spasi
     *
     * @param resource   the resource to read
     * @param bufferSize the initial buffer size
     *
     * @return the resource data
     *
     * @throws IOException if an IO error occurs
     */
    public static ByteBuffer ioResourceToByteBuffer(String resource, int bufferSize) throws IOException {
        ByteBuffer buffer;

        Path path = Paths.get(resource);
        if (Files.isReadable(path)) {
            try (SeekableByteChannel fc = Files.newByteChannel(path)) {
                buffer = MemoryUtil.memAlloc((int)fc.size() + 1);
                while (fc.read(buffer) != -1) {
                    ;
                }
            }
        } else {
            try (
                    InputStream source = IOUtils.class.getClassLoader().getResourceAsStream(resource);
                    ReadableByteChannel rbc = Channels.newChannel(source)
            ) {
                buffer = MemoryUtil.memAlloc(bufferSize);

                while (true) {
                    int bytes = rbc.read(buffer);
                    if (bytes == -1) {
                        break;
                    }
                    if (buffer.remaining() == 0) {
                        buffer = resizeBuffer(buffer, buffer.capacity() * 3 / 2); // 50%
                    }
                }
            }
        }

        buffer.flip();
        return buffer;
    }

    private static ByteBuffer resizeBuffer(ByteBuffer buffer, int newCapacity) {
        ByteBuffer newBuffer = MemoryUtil.memAlloc(newCapacity);
        buffer.flip();
        newBuffer.put(buffer);
        return newBuffer;
    }


}