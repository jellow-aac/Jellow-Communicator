package com.dsource.idc.jellowintl;

import androidx.test.filters.SmallTest;

import com.dsource.idc.jellowintl.utility.IndexSorter;

import org.junit.Test;

@SmallTest
public class IndexSorterTest {

    @Test
    public void isPreferencesSortedCorrectly(){
        Integer[] prefArray = {4, 6, 2, 5, 8, 0, 1, 11, 45, 2, 77};
        IndexSorter<Integer> is = new IndexSorter<Integer>(prefArray);
        is.sort();
        Integer[] sortArray = is.getIndexes();
        Integer[] expectedSortArray = {10, 8, 7, 4, 1, 3, 0, 2, 9, 6, 5};
        for (int i = 0; i < sortArray.length; i++) {
            assert sortArray[i] == expectedSortArray[i];
        }
        assert true;
    }
}