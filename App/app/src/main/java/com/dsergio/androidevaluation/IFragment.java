package com.dsergio.androidevaluation;

/**
 * Fragment Interface
 *
 */
public interface IFragment {

    String getCurrentFragmentFileName();
    String getNextFragmentFileName();
    void setNextFragmentFileName();
    void setCurrentFragmentFileName();

}
