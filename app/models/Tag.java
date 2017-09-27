package models;
 
import java.util.*;

import org.mongodb.morphia.annotations.Entity;

import com.google.common.collect.Maps;

import play.data.validation.Required;
import play.modules.morphia.Model;
 
@Entity
public class Tag extends Model implements Comparable<Tag> {
 
	@Required
    public String name;
 
    private Tag(String name) {
        this.name = name;
    }
 
    public String toString() {
        return name;
    }
 
    public int compareTo(Tag otherTag) {
        return name.compareTo(otherTag.name);
    }
    
    public static Tag findOrCreateByName(String name) {
    	Tag tag = Tag.q().filter("name", name).get();
        if(tag == null) {
            tag = new Tag(name);
            tag.save();
        }
        return tag;
    }
    
    public static List<Map> getCloud() {
    	 List<Map> result = countOccurrences(Tag.q().asList());
    	
    	/*
        List<Map> result = Tag.find(
            "select new map(t.name as tag, count(p.id) as pound) from Post p join p.tags as t group by t.name order by t.name"
        ).fetch();*/
    	 
        return result;
    }
    
    
    @SuppressWarnings("null")
	private static List<Map> countOccurrences(List<Model> list){
    	List<Map> listMap = null;

        for(Model tag: list){
        	
        	int occurrences = Collections.frequency(list, tag);
        	Map<Object, Integer> occurrenceMap = Maps.newHashMap();
        	occurrenceMap.put(tag, occurrences);
        	listMap.add(occurrenceMap);
        }
        
        return listMap;
   }
}