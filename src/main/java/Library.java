import java.util.logging.Logger;

import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.factory.Nd4j;

/*
 * This Java source file was generated by the Gradle 'init' task.
 */
public class Library {
   final static int NUM_SAMPLES = 4;

   public static void main(String[] args) {
      DenseLayer inputLayer = new DenseLayer.Builder()
            .nIn(2)
            .nOut(3)
            .name("Input")
            .build();

    DenseLayer hiddenLayer = new DenseLayer.Builder()
            .nIn(3)
            .nOut(2)
            .name("Hidden").build();

    OutputLayer outputLayer = new OutputLayer.Builder()
            .nIn(2)
            .nOut(2)
            .name("Output")
            .activation(Activation.SOFTMAX)
            .build();
    System.out.println("Layers are created");

    NeuralNetConfiguration.Builder nncBuilder = new NeuralNetConfiguration.Builder()
    .iterations(10000)
    .learningRate(0.01);

    NeuralNetConfiguration.ListBuilder listBuilder = nncBuilder.list()
    .layer(0, inputLayer)
    .layer(1, hiddenLayer)
    .layer(2, outputLayer)
            .backprop(true);

    MultiLayerNetwork myNetwork = new MultiLayerNetwork(listBuilder.build());
    System.out.println("Network init");
    myNetwork.init();

    INDArray trainingInputs = Nd4j.zeros(NUM_SAMPLES, inputLayer.getNIn());
    INDArray trainingOutputs = Nd4j.zeros(NUM_SAMPLES, outputLayer.getNOut());

    // If 0,0 show 0
    trainingInputs.putScalar(new int[]{0,0}, 0);
    trainingInputs.putScalar(new int[]{0,1}, 0);
    trainingOutputs.putScalar(new int[]{0,0}, 0);

    //If 0,1 show 1
    trainingInputs.putScalar(new int[]{1,0}, 0);
    trainingInputs.putScalar(new int[]{1,1}, 1);
    trainingOutputs.putScalar(new int[]{1,0}, 1);

    //If 1,0 show 1
    trainingInputs.putScalar(new int[]{2,0}, 1);
    trainingInputs.putScalar(new int[]{2,1}, 0);
    trainingOutputs.putScalar(new int[]{2,0}, 1);

    //If 1,1 show 0
    trainingInputs.putScalar(new int[]{3,0}, 1);
    trainingInputs.putScalar(new int[]{3,1}, 1);
    trainingOutputs.putScalar(new int[]{3,0}, 0);

    DataSet myData = new DataSet(trainingInputs, trainingOutputs);

    System.out.println("Fitting");
    myNetwork.fit(myData);

    System.out.println("Testing");
    // Create input
    INDArray actualInput = Nd4j.zeros(1,2);
    actualInput.putScalar(new int[]{0,0}, 1);
    actualInput.putScalar(new int[]{0,1}, 1);

    //Generate output
    INDArray actualOutput = myNetwork.output(actualInput);
    Logger.getLogger("").info("myNetwork Output: " +  actualOutput.toString());

    System.out.println(actualOutput.toString());
   }
}
