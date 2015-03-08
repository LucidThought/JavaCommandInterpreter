import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;


public class Wump {

	/*
	* See http://stackoverflow.com/questions/1857775/getting-a-list-of-accessible-methods-for-a-given-class-via-reflection
	*/

	public static Method[] getAccessibleMethods(Class mClass) {
		List<Method> result = new ArrayList<Method>();
		//while (mClass != null) {
			for (Method method : mClass.getDeclaredMethods()) {
			    int modifiers = method.getModifiers();
			    if (Modifier.isPublic(modifiers) || Modifier.isProtected(modifiers)) {
				result.add(method);
			    }
			}

			//mClass = mClass.getSuperclass();
		//}

		return result.toArray(new Method[result.size()]);
	}




	public static void printMethods(Method[] methods) {
		for (Method method : methods) {
			System.out.print("(" + method.getName());
			returnParams(method);
			System.out.println(") : " + method.getReturnType().getName());
		}
	}




	public static void returnParams(Method method) {
		Class[] parameterType = method.getParameterTypes();
		for (Class type : parameterType) {
			if (type.getName().equals("java.lang.String")){
				System.out.print(" String");
			} else
				System.out.print(" " + type.getName());
		}
	}

}
