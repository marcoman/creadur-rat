package org.apache.rat.configuration;

import java.util.SortedSet;

import org.apache.rat.license.ILicense;
import org.apache.rat.license.ILicenseFamily;

class ILicenseProxy implements ILicense {
    private final String proxyId;
    private ILicense wrapped;
    private final SortedSet<ILicense> licenses;

    static public ILicense create(String proxyId, SortedSet<ILicense> licenses) {
        ILicense result = ILicense.search(proxyId, licenses);
        if (result == null) {
            result = new ILicenseProxy(proxyId, licenses);
        }
        return result;
    }

    ILicenseProxy(String proxyId, SortedSet<ILicense> licenses) {
        this.proxyId = proxyId;
        this.licenses = licenses;
    }

    private void checkProxy() {
        if (wrapped == null) {
            wrapped = ILicense.search(proxyId, licenses);
            if (wrapped == null) {
                throw new IllegalStateException(String.format("%s is not a valid family category", proxyId));
            }
        }
    }

    @Override
    public ILicenseFamily getLicenseFamily() {
        checkProxy();
        return wrapped.getLicenseFamily();
    }

    @Override
    public String getNotes() {
        checkProxy();
        return wrapped.getNotes();
    }

    @Override
    public ILicense derivedFrom() {
        checkProxy();
        return wrapped.derivedFrom();
    }

    @Override
    public String getId() {
        checkProxy();
        return wrapped.getId();
    }

    @Override
    public void reset() {
        checkProxy();
        wrapped.reset();
    }

    @Override
    public boolean matches(String line) {
        checkProxy();
        return wrapped.matches(line);
    }

    @Override
    public int compareTo(ILicense arg0) {
        return ILicense.getComparator().compare(this, arg0);
    }
}
