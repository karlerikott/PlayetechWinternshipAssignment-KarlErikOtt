public class Match {
        public Match(String id, double return_a, double return_b, String result) {
                this.id = id;
                this.return_a = return_a;
                this.return_b = return_b;
                this.result = result;
        }

        String id;
        double return_a;
        double return_b;
        String result;

        public double getReturnOdds(String option){
                if(option.equals("A")){
                        return return_a;
                }
                return return_b;
        }


        public String getResult() {
                return result;
        }
}
