package club.swdev.webapp.storage;

import club.swdev.webapp.exception.StorageException;
import club.swdev.webapp.model.Resume;
import org.junit.jupiter.api.Test;

import static club.swdev.webapp.storage.AbstractArrayStorage.STORAGE_CAPACITY;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

public abstract class AbstractArrayStorageTest extends AbstractStorageTest {
    public AbstractArrayStorageTest(Storage storage) {
        super(storage);
    }

    @Test
    public void saveBeyondCapacity() {
        try {
            for (int i = storage.size(); i < STORAGE_CAPACITY; i++) {
                storage.save(new Resume("FakeName"));
            }
        } catch (StorageException e) {
            fail("Error: storage overflow within storage capacity");
        }
        assertThrows(StorageException.class, () -> storage.save(new Resume("Name")));
    }

    @Override
    public void prepareToSave() {
        storage.clear();
    }

    @Override
    public void checkSizeAfterSave() {
        assertSize(1);
    }
}