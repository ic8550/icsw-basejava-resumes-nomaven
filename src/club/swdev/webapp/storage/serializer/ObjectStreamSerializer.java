package club.swdev.webapp.storage.serializer;

import club.swdev.webapp.exception.StorageException;
import club.swdev.webapp.model.Resume;

import java.io.*;

public class ObjectStreamSerializer implements StreamSerializer {
    @Override
    public Resume doRead(InputStream inputStream) throws IOException {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(inputStream)) {
            return (Resume) objectInputStream.readObject();
        } catch (ClassNotFoundException e) {
            throw new StorageException("Error reading resume from  input stream", null, e);
        }
    }

    @Override
    public void doWrite(Resume resume, OutputStream outputStream) throws IOException {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream)) {
            objectOutputStream.writeObject(resume);
        }
    }
}
