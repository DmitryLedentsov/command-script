package script;

import java.util.Map;

import exceptions.ScriptException;
@FunctionalInterface
public interface Command {
    public void call(Map<String,String> args) throws ScriptException;
}
