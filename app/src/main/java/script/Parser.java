package script;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import exceptions.ScriptException;

public class Parser {


    private Map<String, Command> commands;
    private Map<String, String> ctx;
    public Parser(Map<String, Command> t){
        commands = t;
    }
    public Parser(){
        commands = new HashMap<>();
        ctx = new HashMap<>();
    }
    public void registerComand(String name, Command cmd){
        commands.put(name, cmd);
    }
    public void setVar(String name, String val){
        if(!ctx.containsKey(name)) ctx.put(name, val);
        else{
            ctx.replace(name, val);
        }
    }
    public String getVar(String name){
        if(!ctx.containsKey(name)) throw new ScriptException("var " + name + "is not defined");
        return ctx.get(name);
    }
    public void executeLine(String line){
        
        if(line==null || line.trim().equals("")|| line.trim().split("\n").length==0) throw new ScriptException("empty script");
        line = line.replace('\n', ' ');
        line = line.trim();
        if(line.trim().equals("")) throw new ScriptException("empty string found");
        if(line.trim().startsWith("#")) return;
        if(line.trim().startsWith("$")){
            line = line.trim().substring(1);
            String pair[] = line.trim().split(" ",2);
            if (pair.length!=2) throw new ScriptException("wrong variable format, should be <name> space <data>");
            String name = pair[0].trim();
            String data = pair[1].trim();
            setVar(name, data);
            return;
        }
        String pair[] = line.trim().split(" ",2);
        if (pair.length!=2) throw new ScriptException("wrong argument format, should be <name> space <data>(or ! if no args specified)");
        String typeKey = pair[0].trim();
        String data = pair[1].trim();
        
        if(!commands.containsKey(typeKey)) throw new ScriptException("no such command "+ typeKey);
        
        String args[] = data.split(",");
    
        
        Map<String,String> argMap = new HashMap<>();
        if(!data.trim().equals("!")){
            for(String arg: args){
                String argPair[] = arg.trim().split(" ",2);
                
                if(argPair.length==1) throw new ScriptException("wrong arg format " + argPair[0]);
                if (argPair.length!=2) throw new ScriptException("wrong arg format");
            
                String argKey = argPair[0].trim();
                String argData = argPair[1].trim();
                if(argData.startsWith("$")){
                    argData = argData.substring(1);
                    argData = getVar(argData);
                }
                //String argDataArray[] = argData.split(" ");
                argMap.put(argKey, argData);
            }
        }
        try{
            commands.get(typeKey).call(argMap);
        } catch(NumberFormatException e){
            throw new ScriptException("wrong number format");
        }
                    
        
    }
    public void execute(String code) throws ScriptException{
        //List<Object<?>> objects = new LinkedList<>();
        if(code==null || code.trim().equals("")|| code.trim().split("\n").length==0) throw new ScriptException("empty script");
        for(String line: code.split("\n")){
            executeLine(line);
        }

    }

}
