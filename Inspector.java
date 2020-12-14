
/**
 * CPSC 501
 * Inspector starter class
 *
 * @author Jason Li
 * UCID 10158349
 */

import java.lang.reflect.*;
import java.util.ArrayList;


public class Inspector {

    public void inspect(Object obj, boolean recursive) {
        Class c = obj.getClass();
        inspectClass(c, obj, recursive, 0);
    }

    private void inspectClass(Class c, Object obj, boolean recursive, int depth) {
    	//need this variable in order to format the output
    	int d = depth;
    	Class objClass = c;
      //need a list of objects to traverse
    	ArrayList<Field> objList = new ArrayList<Field>();

    	//1. The name of the declaring class
    	System.out.println();
    	System.out.println("Declaring class: " + objClass.getSimpleName());

    	//2. The name of the immediate super-class
		  if (objClass.getSuperclass() != null) {
			inspectSuperclass(obj, objClass, objList, d++);
		  }
		  //3. The name of each interface the class implements
		  inspectInterfaces(obj, objClass, d++);
		  //4. The constructors the class declares
		  inspectConstructors(obj, objClass, d++);
		  //5/ The methods the class declares
		  inspectMethods(obj, objClass, d++);
		  //6. The fields the class declares
      inspectFields(obj, objClass, objList, d++);
		  inspectFieldClasses(obj, objClass, objList, recursive, depth++);
      }

      private void inspectSuperclass(Object obj, Class objClass, ArrayList<Field> objList, int depth) {

      System.out.println();
      int d = depth;
      formattingFunc(d);
		  System.out.println("The name of the immediate super-class: " + objClass.getSuperclass().getSimpleName());
      d++;

      Class superclass = objClass.getSuperclass();
  		inspectMethods(obj, superclass, d);
  		inspectConstructors(obj, superclass, d);
  		inspectFields(obj, superclass, new ArrayList<Field>(), d);
    }

    private void inspectInterfaces(Object obj, Class objClass, int depth) {

    	int d = depth;
		  System.out.println();
  		formattingFunc(d);
  		System.out.println("The name of each interface: ");
  		d++;

      //interate through every interface related to object
  		Class[] interfaceList = objClass.getInterfaces();
  		if (interfaceList.length > 0) {
  			for (int i = 0; i < interfaceList.length; i++) {
  				System.out.println();
  				formattingFunc(d++);
  				System.out.println("Interface: " + interfaceList[i].getName());
  				inspectMethods(obj, interfaceList[i], d  +2);
  				inspectConstructors(obj, interfaceList[i], d + 2);
  			}
  		}
    }

	private void inspectConstructors(Object obj, Class objClass, int depth) {

    int d = depth;
		System.out.println();
		formattingFunc(d);
		System.out.println(objClass.getSimpleName() + " constructor methods: ");

    //iterate through every constructor related to the object
		Constructor[] constructorsList = objClass.getConstructors();
		if (constructorsList.length > 0) {
			for (int i = 0; i < constructorsList.length; i++) {
				Constructor constructorMethod = constructorsList[i];
				Class[] listOfParams = constructorsList[i].getParameterTypes();
				String parametersString = "";

        //handles when there are no parameters for the constructor
				if (listOfParams.length == 0) {parametersString = "none";}

        //handles when there are parameters for the constructor
				else
					for(int j = 0; j < listOfParams.length; j++) {
						parametersString = parametersString + listOfParams[j].getSimpleName() + " ";
					}

				System.out.println();
				formattingFunc(d++);

				//Print out the name, parameters, and modifiers for constructors
				System.out.println("Constructor: " + constructorMethod.getName() +
        "\n\t Parameters: " + parametersString +
        "\n\t Modifiers: " + Modifier.toString(constructorMethod.getModifiers()));
        System.out.println();
			  }
		  }
    }

    private void inspectMethods(Object obj, Class objClass, int depth) {

      int d = depth++;
  		System.out.println();
  		formattingFunc(d++);
  		System.out.println(objClass.getSimpleName() + " methods:");
  		System.out.println();

      //iterate through all the methods related to the object
  		Method[] methodsList = objClass.getDeclaredMethods();
  		if (methodsList.length >= 1) {
  			for (int i = 0; i < methodsList.length; i++) {
  				Method method = methodsList[i];
  				String methodParameters = getMethodParameters(method);
  				String methodExceptions = getMethodExceptions(method);
  				formattingFunc(d);

  				//print name, exceptions, parameters, return type and modifiers
  				System.out.println("Method name: '" + method.getName() +
           "\n\t Exception Thrown: " + methodExceptions +
           "\n\t Parameter Types: " + methodParameters +
           "\n\t Return-type: " + method.getReturnType() +
           "\n\t Modifiers: " + Modifier.toString(method.getModifiers()));
           System.out.println();
			  }
		  }
    }

    private void inspectFields(Object obj, Class objClass, ArrayList<Field> objList, int depth) {
    	int d = depth;
  		System.out.println();
  		formattingFunc(d);
  		System.out.println(objClass.getSimpleName() + " fields:");
  		System.out.println();
  		d++;

  		if (objClass.getDeclaredFields().length >= 1) {
  			Field[] fieldsList = objClass.getDeclaredFields();
  			for (int i = 0; i < fieldsList.length; i++) {
  				try{
    				Field field = fieldsList[i];
    				formattingFunc(d);
    				//make sure private fields are accessed
    				field.setAccessible(true);

				//Handler for arrays
				Object fieldObj = field.get(obj);
				if(field.getType().isArray()) {
					//print out the name, type, modifier, and current value
					System.out.println("Field: " + field.getName() +
					"\n\t Type: " + field.getType().getComponentType() +
					"\n\t Modifiers: " + Modifier.toString(field.getModifiers()));
          System.out.println();

					Object[] objArray;

					if(fieldObj instanceof Object[]) {objArray =  (Object[]) fieldObj;}
					else{
						int arrayLength = Array.getLength(fieldObj);
						objArray = new Object[arrayLength];
						for(int n = 0; n < arrayLength; n++){
							objArray[n] = Array.get(fieldObj, n);
						}
					}

					for(int j =0; j < objArray.length; j++) {
			      Object index = objArray[j];
            String s = null;
            if(index != null)
              if(index instanceof Class) {
                Class ci = index.getClass();
                s = ci.getName() + " " + index.hashCode();
			        }
              else{
               s =  index.toString();
  			       formattingFunc(d+1);
  			       System.out.println("[" + j + "]: " + s);
             }
           }
         }

         //Everything else handler
         else {
  				Object currentValue = field.get(obj);
  				//print name, type, modifier, current value
  				System.out.print("Field: " + field.getName() +
  				"\n\t Type: " + field.getType().getComponentType() +
  				"\n\t Modifier: "	+ Modifier.toString(field.getModifiers()));

  				if(currentValue != null) {
  				System.out.println("\n\t Current Value: " + currentValue.toString());
  				}

					else {
						System.out.println("\n\t Current Value: none");
					}
        }
      }
      catch(Exception e){
        e.printStackTrace();
      }
			}
		}

		System.out.println();
		if (objClass.getSuperclass() != null)
			inspectFields(obj, objClass.getSuperclass(), objList, depth);
    }

	private void formattingFunc(int depth) {
		for(int i = 0; i < depth; i++) {
			System.out.print("\t");
		}
	}

	private String getMethodExceptions(Method method) {
		Class[] exceptions = method.getExceptionTypes();
		String exceptionString = "";

    //handler for when the method has not exceptions
		if (exceptions.length == 0) {
      exceptionString = "none";
      System.out.println();
    }

    //nadler for when the method does have exceptions
		else
			for (Class exception : exceptions) {
				exceptionString = exceptionString + exception.getSimpleName() + " ";
        System.out.println();
			}
		return exceptionString;
	}

	private String getMethodParameters(Method method) {
		Class[] parameters = method.getParameterTypes();
		String parametersString = "";

    //hander for when the method has not parameters
		if (parameters.length == 0) {parametersString = "none";}

    //hander for when the method has paramerts
		else
			for (Class parameter : parameters) {
				parametersString = parametersString + parameter.getSimpleName() + " ";
			}
		return parametersString;
	}

	private void inspectFieldClasses(Object obj, Class objClass, ArrayList<Field> objList, Boolean recursive, int depth) {
    int d = depth;
		System.out.println();
		formattingFunc(d);
		System.out.println(objClass.getSimpleName() + "Fields:");
		System.out.println();
		d++;

		if (objClass.getDeclaredFields().length >= 1) {
			Field[] fieldsList = objClass.getDeclaredFields();

			for (int i = 0; i < fieldsList.length; i++) {
        //ensure the private fields are accessible
				fieldsList[i].setAccessible(true);
				Class fieldType = fieldsList[i].getType();

				//if it is not a primitive, the object is a class
				if(!fieldType.isPrimitive()) {
          System.out.println("Field: " + fieldsList[i].getName() + " Object");
          try {
						if(fieldsList[i].get(obj) != null) {
						    	inspect(fieldsList[i].get(obj), recursive);
						}
						else {
							System.out.println("The object is not installed or null.");
						}
					}
          catch (IllegalArgumentException|IllegalAccessException e) {
						e.printStackTrace();
					}
        }
			}
		}
	}
}
