
public class OuterClass {
	
	public enum Direction{
		A("asddas"),B,C,D;
		
		Direction(){}
		
		Direction(String str){
			
		}
	}

	class InnerClass{
		public void foo(){
			System.out.println("foo 호출됨");
		}
	}
	
	static class SInnerClass{
		public void bar(){
			System.out.println("bar 호출됨");
		}
		
	}

}
