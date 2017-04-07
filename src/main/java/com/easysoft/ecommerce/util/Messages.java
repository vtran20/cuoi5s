package com.easysoft.ecommerce.util;

import java.util.ArrayList;
import java.util.List;

/**
 * User: vtran
 * Date: Aug 4, 2010
 * Time: 11:44:11 AM
 */
public class Messages {

    private List<String> infos = null;
    private List<String> warnings = null;
    private List<String> errors = null;

    public List<String> getInfos() {
        return infos;
    }

    public List<String> getWarnings() {
        return warnings;
    }

    public List<String> getErrors() {
        return errors;
    }

    /**
     * @param key
     * @param value
     * @deprecated Using addInfo (String value)
     */
    public void addInfo(String key, String value) {
        addInfo(value);
    }

    public void addInfo(String value) {
        if (infos == null) {
            infos = new ArrayList<String>();
        }
        infos.add(value);
    }

    /**
     * @param key
     * @param value
     * @deprecated Using addWarning (String value)
     */
    public void addWarning(String key, String value) {
        addWarning(value);
    }

    public void addWarning(String value) {
        if (warnings == null) {
            warnings = new ArrayList<String>();
        }
        warnings.add(value);
    }

    /**
     * @param key
     * @param value
     * @deprecated Using addError (String value)
     */
    public void addError(String key, String value) {
        addError(value);
    }

    public void addError(String value) {
        if (errors == null) {
            errors = new ArrayList<String>();
        }
        errors.add(value);
    }

    public boolean isEmpty () {
        return infos == null && warnings == null && errors == null;
    }

    public void clean () {
        if (infos != null) {
            infos.clear();
        }
        if (warnings != null) {
            warnings.clear();
        }
        if (errors != null) {
            errors.clear();
        }
    }

    public void addAll (Messages messages) {
        if (messages != null) {
            // add Info
            if (messages.getInfos() != null) {
                if (this.infos != null) {
                    this.infos.addAll(messages.getInfos());
                } else {
                    this.infos = messages.getInfos();
                }
            }
            // add Warning
            if (messages.getWarnings() != null) {
                if (this.warnings != null) {
                    this.warnings.addAll(messages.getWarnings());
                } else {
                    this.warnings = messages.getWarnings();
                }
            }
            // add Errors
            if (messages.getErrors() != null) {
                if (this.errors != null) {
                    this.errors.addAll(messages.getErrors());
                } else {
                    this.errors = messages.getErrors();
                }
            }
        }
    }

    public boolean hasErrors () {
        return this.errors != null && this.errors.size() > 0;
    }
    public boolean hasInfors () {
        return this.infos != null && this.infos.size() > 0;
    }
    public boolean hasWarning () {
        return this.warnings != null && this.warnings.size() > 0;
    }

    public String toString () {
        StringBuilder result = new StringBuilder();
        if (hasInfors()) {
            result.append("<div class=\"alert alert-success\">");
            result.append("<button type=\"button\" class=\"close\" data-dismiss=\"alert\">x</button>");
            for (String value: infos) {
                result.append(value).append("<br>");
            }
            result.append("</div>");
        }
        if (hasWarning()) {
            result.append("<div class=\"alert alert-danger\">");
            result.append("<button type=\"button\" class=\"close\" data-dismiss=\"alert\">x</button>");
            for (String value: warnings) {
                result.append(value).append("<br>");
            }
            result.append("</div>");
        }
        if (hasErrors()) {
            result.append("<div class=\"alert alert-danger\">");
            result.append("<button type=\"button\" class=\"close\" data-dismiss=\"alert\">x</button>");
            for (String value: errors) {
                result.append(value).append("<br>");
            }
            result.append("</div>");
        }
        return result.toString();
    }
    public String toText () {
        StringBuilder result = new StringBuilder();
        if (hasInfors()) {
            for (String value: infos) {
                result.append(value).append(". ");
            }
        }
        if (hasWarning()) {
            for (String value: warnings) {
                result.append(value).append(". ");
            }
        }
        if (hasErrors()) {
            for (String value: errors) {
                result.append(value).append(". ");
            }
        }
        return result.toString();
    }
}
