package club.swdev.webapp.storage;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({ArrayStorageTest.class, SortedArrayStorageTest.class, ListStorageTest.class, MapStorageTest.class})

public class StorageTest {
}