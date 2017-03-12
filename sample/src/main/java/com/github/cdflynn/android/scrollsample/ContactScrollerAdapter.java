package com.github.cdflynn.android.scrollsample;

import com.github.cdflynn.android.scrollsample.mock.Contact;
import com.github.cdflynn.android.scrollsample.mock.Section;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cdflynn.android.library.scroller.SectionScrollAdapter;

public class ContactScrollerAdapter implements SectionScrollAdapter {

    private List<Contact> mContacts;
    private List<Section> mSections;

    public ContactScrollerAdapter(List<Contact> contacts) {
        initWithContacts(contacts);
    }

    @Override
    public int getSectionCount() {
        return mSections.size();
    }

    @Override
    public String getSectionTitle(int position) {
        return mSections.get(position).getTitle();
    }

    @Override
    public int getSectionWeight(int position) {
        return mSections.get(position).getWeight();
    }

    public Section fromSectionIndex( int sectionIndex) {
        return mSections.get(sectionIndex);
    }

    public Section fromItemIndex(int itemIndex) {
        for (Section s : mSections) {
            final int range = s.getIndex() + s.getWeight();
            if (itemIndex < range) {
                return s;
            }
        }
        return mSections.get(mSections.size() - 1);
    }

    public int positionFromSection(int sectionIndex) {
        return mSections.get(sectionIndex).getIndex();
    }

    public int sectionFromPosition(int positionIndex) {
        Section s = null;
        for (int i = 0; i < mSections.size(); i++) {
            s = mSections.get(i);
            final int range = s.getIndex() + s.getWeight();
            if (positionIndex < range) {
                return i;
            }
        }
        return mSections.size() - 1;
    }

    private void initWithContacts(List<Contact> contacts) {
        mContacts = contacts;
        mSections = new ArrayList<>();
        Collections.sort(mContacts, Contact.COMPARATOR);
        String sectionTitle = null;
        Contact contact;
        int itemCount = 0;
        for (int i = 0; i < mContacts.size(); i++) {
            contact = mContacts.get(i);
            String firstLetter = contact.getFirstName().substring(0, 1);

            if (sectionTitle == null) {
                sectionTitle = firstLetter;
            }
            if (sectionTitle.compareTo(firstLetter) == 0) {
                itemCount++;
                continue;
            }

            mSections.add(new Section(i - itemCount, sectionTitle, itemCount));
            sectionTitle = firstLetter;
            itemCount = 1;
        }
    }
}
