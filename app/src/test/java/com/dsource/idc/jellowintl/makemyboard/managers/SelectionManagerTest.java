package com.dsource.idc.jellowintl.makemyboard.managers;

import com.dsource.idc.jellowintl.models.JellowIcon;

import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.mockito.Mockito.mock;

public class SelectionManagerTest {

    private SelectionManager SUT;

    @Before
    public void setup(){
        SUT = SelectionManager.getInstance();
    }

    @Test
    public void getInstance_checksIfProperInstanceIsReturned() {
        assertThat(SUT.getInstance(), instanceOf(SelectionManager.class));
    }

    @Test
    public void addIconToList() {
        SUT.clearList();
        JellowIcon icon1 = mock(JellowIcon.class);
        SUT.addIconToList(icon1);
        //Check if added successfully
        assert SUT.isPresent(icon1);
    }

    @Test
    public void removeIconFromList() {
        SUT.clearList();
        JellowIcon icon = mock(JellowIcon.class);
        SUT.addIconToList(icon);
        SUT.removeIconFromList(icon);
        //Icon should not be present in the list
        assert !SUT.isPresent(icon);
    }

    @Test
    public void setList() {
        SUT.clearList();
        ArrayList<JellowIcon> list = new ArrayList<>();
        for(int i=0;i<10;i++){
            list.add(mock(JellowIcon.class));
        }
        SUT.setList(list);
        //Check for the first element
        assert SUT.getList().get(0).equals(list.get(0));
        //Check for the complete list
        assert SUT.getList().equals(list);
        //Check for the last element
        assert SUT.getList().get(9).equals(list.get(9));

    }

    @Test
    public void getList() {
        SUT.clearList();
        ArrayList<JellowIcon> list = new ArrayList<>();
        for(int i=0;i<10;i++){
            list.add(mock(JellowIcon.class));
        }
        SUT.setList(list);
        assert SUT.getList().equals(list);
    }

    @Test
    public void selectAll_True() {
        SUT.clearList();
        ArrayList<JellowIcon> list = new ArrayList<>();
        for(int i=0;i<10;i++){
            list.add(mock(JellowIcon.class));
        }
        SUT.selectAll(true,list);
        assert SUT.isSublist(list);
    }
    @Test
    public void selectAll_False() {
        SUT.clearList();
        ArrayList<JellowIcon> list = new ArrayList<>();
        for(int i=0;i<10;i++){
            list.add(mock(JellowIcon.class));
        }
        SUT.selectAll(false,list);
        assert !SUT.isSublist(list);
    }

    @Test
    public void delete() {
        SUT.clearList();
        ArrayList<JellowIcon> list = new ArrayList<>();
        for(int i=0;i<10;i++){
            list.add(mock(JellowIcon.class));
        }
        SUT.setList(list);

        SUT.delete();

        assert SUT.getList().size()==0;
    }

    @Test
    public void addIcon_isPresent_shouldReturnTrue() {
        SUT.clearList();
        JellowIcon icon = mock(JellowIcon.class);
        SUT.addIconToList(icon);
        assert SUT.isPresent(icon);
    }

    @Test
    public void addIcon_isPresent_shouldReturnFalse() {
        SUT.clearList();
        JellowIcon icon = mock(JellowIcon.class);
        assert !SUT.isPresent(icon);
    }

    @Test
    public void isSublist_Positive() {
        SUT.clearList();
        ArrayList<JellowIcon> list = new ArrayList<>();
        for(int i=0;i<10;i++){
            list.add(mock(JellowIcon.class));
        }
        SUT.setList(list);
        ArrayList<JellowIcon> sublist = new ArrayList<>();
        for(int i=0;i<10;i++){
            list.add(mock(JellowIcon.class));
        }
        //Call select all method to add all the sublist icons ot the list
        SUT.selectAll(true,sublist);
        assert SUT.isSublist(sublist);
    }
    @Test
    public void isSublist_Negative() {
        SUT.clearList();
        ArrayList<JellowIcon> list = new ArrayList<>();
        for(int i=0;i<10;i++){
            list.add(mock(JellowIcon.class));
        }
        SUT.setList(list);

        ArrayList<JellowIcon> sublist = new ArrayList<>();
        for(int i=0;i<10;i++){
            sublist.add(mock(JellowIcon.class));
        }
        Boolean d  = SUT.isSublist(sublist);
        //Here we didn't add the list to the SUT
        assert !SUT.isSublist(sublist);
    }

    @Test
    public void containsAny() {
        SUT.clearList();
        ArrayList<JellowIcon> list = new ArrayList<>();
        for(int i=0;i<10;i++){
            list.add(mock(JellowIcon.class));
        }
        SUT.addIconToList(list.get(4));

        assert SUT.containsAny(list);

    }
}