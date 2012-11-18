(ns sicp.ch1)

;;; Exercise 1.2

(/ (+ 5 4 (- 2 (- 3 (+ 6 (/ 4 5)))))
   (* 3 (- 6 2) (- 2 7)))               ; -37/150

;;; Exercise 1.3

(defn sum-square-larger [a b c]
  (defn square [n] (* n n))
  (- (apply + (map square (vector a b c)))
     (square (min a b c))))

;;; Exercise 1.7

(defn sqrt [n]
  (defn good-enough? [old-guess new-guess]
    (< (/ (Math/abs (- old-guess new-guess))
          old-guess)
       0.001))
  (defn improve [guess]
    (defn average [x y] (/ (+ x y) 2))
    (average guess (/ n guess)))
  (defn help [guess]
    (let [new (improve guess)]
      (if (good-enough? guess new) new
          (help new))))
  (help 1.0))

;;; Exercise 1.8

(defn cbrt [x]
  (defn cbrt-iter [guess x]
    (defn cube [x]
      (Math/pow x 3))
    (defn good-enough? [guess x]
      (< (Math/abs (- (cube guess) x)) 0.001))
    (defn improve [guess x]
      (/ (+ (/ x (* guess guess))
            (* 2 guess))
         3))
    (if (good-enough? guess x)
      guess
      (cbrt-iter (improve guess x) x)))
  (cbrt-iter 1.0 x))

;;; Exercise 1.11

(defn f-rec [n]
  (if (< n 3) n
      (+ (f-rec (- n 1))
         (f-rec (- n 2))
         (f-rec (- n 3)))))

(defn f-iter [n]
  (defn f-help [n1 n2 n3 cnt]
    (if (= cnt n) (+ n1 n2 n3)
        (f-help n2 n3 (+ n1 n2 n3) (inc cnt))))
  (if (< n 3) n
      (f-help 0 1 2 3)))

;;; Exercise 1.12

(defn pascal [row col]
  (cond (= row 0) (if (= col 0) 1 (throw (Exception. "Out of range")))
        (= col 0) 1
        (= col row) 1
        (> row 1) (if (or (< col 0) (> col row))
                    (throw (Exception. "Out of range"))
                    (+ (pascal (dec row) (dec col))
                       (pascal (dec row) col)))
        :else (throw (Exception. "Out of range"))))

(for [row (range 0 10)
      col (range 0 (inc row))]
  (pascal row col))

;;; Exercise 1.16

(defn square [n] (* n n))

(defn fast-expt-iter [b n]
  (defn help [a b n]
    (cond (= n 0) a
          (even? n) (help a (square b) (/ n 2))
          (odd? n) (help (* a b) b (dec n))))
  (help 1 b n))

(every? identity
        (for [x (range 0 10) y (range 0 10)]
          (= (fast-expt-iter x y) (int (Math/pow x y)))))

;;; Exercise 1.17

(defn double [n] (* 2 n))
(defn halve [n]
  (when (even? n) (/ n 2)))

(defn fast-mult-rec [a b]
  (cond (= b 1) a
        (= a 1) b
        (even? b) (double (fast-mult-rec a (halve b)))
        :else (+ a (fast-mult-rec a (dec b)))))

;;; Exercise 1.18

(defn fast-mult-iter [a b]
  (defn help [a b c]
    (cond (= b 0) c
          (even? b) (help (double a) (halve b) c)
          (odd? b) (help a (dec b) (+ c a))))
  (help a b 0))

;;; Exercise 1.19

(defn fib [n]
  (defn fib-iter [a b p q count]
    (cond (= count 0) b
          (even? count) (fib-iter a b
                                  (+ (* p p) (* q q))
                                  (+ (* q q) (* 2 p q))
                                  (/ count 2))
          :else (fib-iter (+ (* b q) (* a q) (* a p))
                          (+ (* b p) (* a q))
                          p
                          q
                          (- count 1))))
  (fib-iter 1 0 0 1 n))

;;; Exercise 1.22

(defn expmod [base exp m]
  "base^exp mod m"
  (cond (= exp 0) 1
        (even? exp) (mod (square (expmod base (/ exp 2) m))
                         m)
        :else (mod (* base (expmod base (dec exp) m))
                   m)))

(defn fermat-test [n]
  "Using the Fermat test for primality, make a guess if n is prime."
  (let [rand (int (inc (rand (dec n))))]
    (= (expmod rand n n) rand)))

(defn fast-prime? [n times]
  (every? identity
          (map fermat-test (take times (repeat n)))))

(defn search-for-primes [from to]
  (let [table (map #(vector % (fast-prime? % 20)) (range from to))]
    (map first (filter second table))))

;;; Exercise 1.23

(defn smallest-divisor [n]
  (defn find-divisor [test]
    (defn next-test [n]
      (if (= n 2) 3
          (+ n 2)))
    (cond (> (square test) n) n
          (= (mod n test) 0) test
          :else (find-divisor (next-test test))))
  (find-divisor 2))

(defn prime? [n]
  (= n (smallest-divisor n)))

;;; Exercise 1.27

(defn fools-fermat? [n]
  (and (not (prime? n))
       (every? #(= (expmod % n n) %)
               (range 1 n))))

;;; Exercise 1.28


