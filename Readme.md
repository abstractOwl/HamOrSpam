# HamOrSpam
A Naive Bayes Classifier to classify spam in text messages

## Usage
`java Classifier trainingSet.txt testSet.txt`

## Notes

For my CS165A MP-2 SMS Classifier project, I implemented the Naive Bayes
learning technique.


First, I decided to normalize all the terms in the message. The classifier
removes all non digit/letter symbols from the input and converts it to lowercase
to eliminate differences caused by punctuation/capitalization. I also mapped
numeric sequences to tokens based on number length. This groups many number
sequences such as area codes and phone numbers, which are unlikely to repeat
otherwise (spammers do not all give the same phone number).


Next, I implemented the Naive Bayes technique by calculating the result of the
probability:

`````
    P(Category | Message) = P(Category) * Product(P(Term_i | Category))
`````

and picking the category with the highest resulting probability.


I found that sometimes, the word would not occur in the training dataset and
thus result in a probability of 0. This, in turn, would cause the entire
probability to result in 0, which is obviously not correct behavior. To
remedy this case, I implemented **Additive Smoothing**, which adds \omega
to the numerator and a multiple of \omega to the denominator to smooth the
estimator.


Finally, I tried to design my project to be generalizable to >2 categories.
At the moment, it is possible to use this Classifier on other datasets by
simply amending the `Types` enum in Main, though it may be interesting to
extract this into an external config file.
