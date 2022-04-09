package club.swdev.webapp.exception;

public class ItemNotPresentInStorageException extends StorageException {
    public ItemNotPresentInStorageException(String uuid) {
        super("Resume " + uuid + " not present in storage", uuid);
    }
}