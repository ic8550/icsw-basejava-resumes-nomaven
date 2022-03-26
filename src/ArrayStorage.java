/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    /**
     * final int MAX_SIZE - storage parameter, the maximum capacity of the storage
     */
    private final int MAX_SIZE = 3;

    Resume[] storage = new Resume[MAX_SIZE];

    /**
     * Size of nonempty (nonnull) part of storage[] -- Number of contiguous
     * nonnull Resumes in the storage[] array, starting from the beginning
     * of storage[].
     */
    int size = 0;

    /**
     * Replaces all nonnull Resumes in storage[] with the null value.
     */
    void clear() {
        for (int i = 0; i < size; i++) {
            storage[i] = null;
        }
        size = 0;
    }

    /**
     * Adds a Resume with a given uuid to the storage[],
     * provided such Resume is not there already.
     */
    void save(Resume resume) {
        String uuid = resume.getUuid();
        if (uuid == null) {
            System.out.println("ERROR: save(): required argument missing");
            return;
        }
        if (size >= MAX_SIZE) {
            System.out.println("ERROR: save(): storage is full; cannot add new resume");
            return;
        }
        String newResumeStringRepresentation = resume.toString();
        for (int i = 0; i < size; i++) {
            if (storage[i].toString().equals(newResumeStringRepresentation)) {
                System.out.println("ERROR: save(): resume with uuid="
                        + "\"" + uuid + "\""
                        + " already present in storage");
                return;
            }
        }
        storage[size] = resume;
        size++;
    }

    /**
     * Adds a Resume with a given uuid to the storage[],
     * provided such Resume is not there already.
     */
    void update(Resume resume) {
        String uuid = resume.getUuid();
        if (uuid == null) {
            System.out.println("ERROR: save(): required argument missing");
            return;
        }
        for (int i = 0; i < size; i++) {
            if (storage[i].getUuid().equals(uuid)) {
                storage[i] = resume;
                return;
            }
        }
        System.out.println("ERROR: update(): resume with uuid="
                + "\"" + uuid + "\""
                + " not found in storage");
    }

    /**
     * @return Resume with a given uuid or null if there is no such Resume in storage[].
     */
    Resume get(String uuid) {
        if (uuid == null) {
            System.out.println("ERROR: get(): required argument missing");
            return null;
        }
        for (int i = 0; i < size; i++) {
            if (storage[i].getUuid().equals(uuid)) {
                return storage[i];
            }
        }
        System.out.println("ERROR: get(): resume with uuid="
                + "\"" + uuid + "\""
                + " not found in storage");
        return null;
    }

    /**
     * Removes a Resume with a given uuid while squeezing the rest of Resumes
     * in the storage[] towards the beginning of the storage so that nonnull Resumes
     * remain contiguous.
     */
    void delete(String uuid) {
        if (uuid == null) {
            System.out.println("ERROR: delete(): required argument missing");
            return;
        }
        if (size == 0) {
            System.out.println("ERROR: delete(): resume with uuid="
                    + "\"" + uuid + "\""
                    + " not found in storage");
            return;
        }
        for (int i = 0; i < size; i++) {
            if (storage[i].getUuid().equals(uuid)) {
                storage[i] = storage[size - 1];
                storage[size - 1] = null;
                size--;
                return;
            }
        }
        System.out.println("ERROR: delete(): resume with uuid="
                + "\"" + uuid + "\""
                + " not found in storage");
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    Resume[] getAll() {
        Resume[] all = new Resume[size];
        for (int i = 0; i < size; i++) {
            all[i] = storage[i];
        }
        return all;
    }

    /**
     * @return int number of contiguous nonnull Resumes of the storage[] array,
     * starting from the beginning of storage[].
     */
    int size() {
        return size;
    }
}
