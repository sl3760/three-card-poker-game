package com.poker.server;

import java.util.ArrayList;



public class Ranking {
	private ArrayList<Integer> RDs;
    private ArrayList<Double> rs;
    private ArrayList<Double> s;
    private double r;
    private double RD0;
    private int t;
    private double q;
    private double RD;
    private double dSquare;

    public Ranking(ArrayList<Integer> RDs, ArrayList<Double> rs,
                    ArrayList<Double> s, double r, double RD0, int t) {
            this.RDs = RDs;
            this.rs = rs;
            this.s = s;
            this.r = r;
            this.RD0 = RD0;
            this.t = t;

            q = Math.log(10) / 400;
            dSquare = dSquare();
            RD = computeRD();
    }

    public double computeRD() {
            double c = Math.sqrt((350 * 350 - 50 * 50) / 365);
            return Math.min(Math.sqrt(RD0 * RD0 + c * c * t), 350);
    }

    public double computeNewRanking() {

            double rightSum = 0;
            for (int i = 0; i < RDs.size(); i++) {
                    double RDi = RDs.get(i);
                    rightSum += g(RDi) * (s.get(i) - E(g(RDi), r, rs.get(i)));
            }

            double rPrime = r + q / (1 / (RD * RD) + 1 / dSquare) * rightSum;

            return rPrime;

    }

    private double g(double RDi) {
            return 1 / (Math.sqrt(1 + 3 * q * q * RDi * RDi / (Math.PI * Math.PI)));
    }

    private double E(double gRDi, double r, double ri) {
            return 1 / (1 + Math.pow(10, (gRDi * (r - ri) / -400)));
    }

    private double dSquare() {
            double result = 0;

            for (int i = 0; i < RDs.size(); i++) {
                    double RDi = RDs.get(i);
                    double ri = rs.get(i);
                    double gRDi = g(RDi);
                    double E = E(gRDi, r, ri);
                    result += q * q * gRDi * gRDi * E * (1 - E);
            }
            return 1 / result;
    }

    public double computeNewRD() {
            return Math.sqrt(1 / (1 / (RD * RD) + 1 / dSquare));
    }

}
