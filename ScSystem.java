package ru.va.progrixivan.visionassistsant;

import android.util.Log;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.dnn.Dnn;
import org.opencv.dnn.Net;
import org.opencv.imgproc.Imgproc;
import org.opencv.utils.Converters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



public class ScSystem {
    private static Net net, tinyYolo, emotionModel, detector, faceModel, busYolo, busNum;
    public static boolean IsNetWorking = false, IsNetPrepared = false;





    private static Mat AOD_NET(Mat frame){
        Sounding.init();
        Imgproc.cvtColor(frame, frame, Imgproc.COLOR_RGBA2RGB);
        //        //Creating a blob
        //['yolo_82', 'yolo_94', 'yolo_106']
        Mat blob = Dnn.blobFromImage(frame, 0.00392, new Size(416, 416), new Scalar(0, 0, 0),/*swapRB*/false, /*crop*/false);

        net.setInput(blob);

        java.util.List<Mat> result = new java.util.ArrayList<Mat>(3);

        List<String> outBlobNames = new java.util.ArrayList<>();
        outBlobNames.add(0, "yolo_82");
        outBlobNames.add(1, "yolo_94");
        outBlobNames.add(2, "yolo_106");

        net.forward(result, outBlobNames);

        //Toast.makeText(getApplicationContext(),"COMPUTED, BABY!" ,Toast.LENGTH_LONG).show();
        //Log.d("YOLO", "COLSSSS!!!! ---->>> "+Integer.toString(result.size())+" ");
        //Log.d("YOLO", "MATRIX! "+frame.toString());

        float confThreshold = 0.25f;
        List<Integer> clsIds = new ArrayList<>();
        List<Float> confs = new ArrayList<>();
        List<Rect> rects = new ArrayList<>();
        for (int i = 0; i < result.size(); ++i) {

            Mat level = result.get(i);
            for (int j = 0; j < level.rows(); ++j) {
                Mat row = level.row(j);
                Mat scores = row.colRange(5, level.cols());
                Core.MinMaxLocResult mm = Core.minMaxLoc(scores);

                float confidence = (float) mm.maxVal;


                Point classIdPoint = mm.maxLoc;


                if (confidence > confThreshold) {
                    int centerX = (int) (row.get(0, 0)[0] * frame.cols());
                    int centerY = (int) (row.get(0, 1)[0] * frame.rows());
                    int width = (int) (row.get(0, 2)[0] * frame.cols());
                    int height = (int) (row.get(0, 3)[0] * frame.rows());
                    int left = centerX - width / 2;
                    int top = centerY - height / 2;

                    clsIds.add((int) classIdPoint.x);
                    confs.add((float) confidence);
                    rects.add(new Rect(left, top, width, height));
                }
            }
        }
        int ArrayLength = confs.size();

        if (ArrayLength >= 1) {
            MainActivity.Window = MainActivity.MAIN_WINDOW;
            // Apply non-maximum suppression procedure.
            float nmsThresh = 0.4f;
            ;
            MatOfFloat confidences = new MatOfFloat(Converters.vector_float_to_Mat(confs));


            Rect[] boxesArray = rects.toArray(new Rect[0]);

            MatOfRect boxes = new MatOfRect(boxesArray);

            MatOfInt indices = new MatOfInt();


            Dnn.NMSBoxes(boxes, confidences, confThreshold, nmsThresh, indices);


            // Draw result boxes:


            int[] ind = indices.toArray();
            for (int i = 0; i < ind.length; ++i) {
                int idx = ind[i];
                Rect box = boxesArray[idx];
                int x = (int) ((box.x + box.width / 2) / (float) (frame.width()) * 100);
                System.out.print(x);
                int idGuy = clsIds.get(idx);
                Imgproc.putText(frame, idGuy + "", box.tl(), Core.FONT_HERSHEY_SIMPLEX, 2, new Scalar(255, 255, 255), 2);
                Imgproc.rectangle(frame, box.tl(), box.br(), new Scalar(0, 0, 255), 2);
                Sounding.AddItem(idGuy, x);
            }


            Imgproc.putText(frame, frame.cols() + "x" + frame.rows(), new Point(50, 50), Core.FONT_HERSHEY_SIMPLEX, 2, new Scalar(0, 255, 255), 2);

            Sounding.PlayObjAssets();
            MainActivity.timer = 100;




        }




        return frame;
    }

    private static void FD_NET(Mat frame) {
        //showFace=false

        ; //!!!!!!!!!!!!!
        Imgproc.cvtColor(frame, frame, Imgproc.COLOR_RGBA2RGB);
        //Log.d("Faces",Integer.toString(detections.size(2))+" "+Integer.toString(detections.size(3)));
        //for (int i = 0; i < result.size(); ++i)
        //Imgproc.resize(frame, frame, new Size(300, 300));//resize the image to match the input size of the model

        //create a 4-dimensional blob from image with NCHW (Number of images in the batch -for training only-, Channel, Height, Width) dimensions order,
        //for more detailes read the official docs at https://docs.opencv.org/trunk/d6/d0f/group__dnn.html#gabd0e76da3c6ad15c08b01ef21ad55dd8
        Mat imageBlob = Dnn.blobFromImage(frame, 1.0, new Size(300, 300), new Scalar(104.0, 177.0, 123.0), true, false, CvType.CV_32F);

        detector.setInput(imageBlob);//set the input to network model
        Mat detections = detector.forward();//feed forward the input to the netwrok to get the output

        int cols = frame.cols();
        int rows = frame.rows();
        double THRESHOLD = 0.5;
        //Log.d("EXPERIMENT", ""+output.get(0,0,Listik));
        //1x1x120x7
//            Log.d("EXPERIMENT2", output.dims()+" "+output.size(0)+" "+output.size(1)+" "+output.size(2)+" "+output.size(3));
//            Mat goodGuy = output.row(0).col(0);
//            double[] num = output.get(0,0);
//
//            Log.d("EXPERIMENT3",goodGuy.dims()+" "+goodGuy.size(0)+" "+goodGuy.size(1));

        detections = detections.reshape(1, (int) detections.total() / 7);
        //Log.d("EXPERIMENT4", detections.dims()+"");
        Log.d("EXPERIMENT5:ROWS", detections.rows() + "");
        for (int i = 0; i < detections.rows(); ++i) {

            double confidence = detections.get(i, 2)[0];
            Log.d("EXPERIMENT6", i + " " + confidence + " " + THRESHOLD);
            if (confidence > THRESHOLD) {
                //int classId = (int)detections.get(i, 1)[0];
                int left = (int) (detections.get(i, 3)[0] * cols);
                int top = (int) (detections.get(i, 4)[0] * rows);
                int right = (int) (detections.get(i, 5)[0] * cols);
                int bottom = (int) (detections.get(i, 6)[0] * rows);
                // Draw rectangle around detected object.

                if (left < 0) {
                    left = 0;
                }
                if (top < 0) {
                    top = 0;
                }
                if (right < 0) {
                    right = 0;
                }
                if (bottom < 0) {
                    bottom = 0;
                }

                int xLim = frame.size(1);
                int yLim = frame.size(0);

                if (left >= xLim) {
                    left = xLim - 2;
                }
                if (right >= xLim) {
                    right = xLim - 2;
                }

                if (top >= yLim) {
                    top = yLim - 2;
                }
                if (bottom >= yLim) {
                    bottom = yLim - 2;
                }


                Rect rectCrop = new Rect(new Point(left, top), new Point(right, bottom));

                Mat face = new Mat(frame, rectCrop);

                Imgproc.rectangle(frame, new Point(left, top), new Point(right, bottom), new Scalar(255, 255, 0), 2);


                //['Age/Softmax', 'Gender/Softmax']


                if(face.cols() < 15 && face.rows() < 15) {

                    continue;

                }

                Mat faceBlob = Dnn.blobFromImage(face, 0.00392, new Size(80, 80), new Scalar(0, 0, 0), true, false);



                java.util.List<Mat> results = new java.util.ArrayList<Mat>(2);

                List<java.lang.String> faceBlobNames = new java.util.ArrayList<>();
                faceBlobNames.add(0, "Age/Softmax");
                faceBlobNames.add(1, "Gender/Softmax");


                faceModel.setInput(faceBlob);
                faceModel.forward(results, faceBlobNames);

//                    Core.MinMaxLocResult lengthScores = Core.minMaxLoc(result.get(0));
//                    Point lengthPoint = lengthScores.maxLoc;
//                    int length = (int)lengthPoint.x;

                Core.MinMaxLocResult ageScores = Core.minMaxLoc(results.get(0));
                Point agePoint = ageScores.maxLoc;
                int age = (int) agePoint.x;
                float ageGuy = (float) ageScores.maxVal;
                int ageConf = (int) (ageGuy * 100);

                List<String> ages = Arrays.asList("0-14", "15-40", "41-60", "60-100");
                String ageText = ages.get(age);


                Core.MinMaxLocResult genderScores = Core.minMaxLoc(results.get(1));
                Point genderPoint = genderScores.maxLoc;
                int gender = (int) genderPoint.x;
                float genderGuy = (float) genderScores.maxVal;
                int genderConf = (int) (genderGuy * 100);


                List<String> genders = Arrays.asList("Female", "Male");
                String genderText = genders.get(gender);


                //['Age/Softmax', 'Gender/Softmax']


//                    embedder.setInput(faceBlob);
//                    Mat vec = embedder.forward();
//
//                    genderModel.setInput(vec);
//                    Mat genderMat = genderModel.forward();
//
//                    ageModel.setInput(vec);
//                    Mat ageMat = ageModel.forward();
//
//                    Core.MinMaxLocResult ageScores = Core.minMaxLoc(ageMat);
//                    float ageConf = (float)ageScores.maxVal;
//                    Point ageId = ageScores.maxLoc;
//
//
//                    Core.MinMaxLocResult genScores = Core.minMaxLoc(genderMat);
//                    float genConf = (float)genScores.maxVal;
//                    Point genId = genScores.maxLoc;


                int xCenter = ((right - left) / 2) + left;


                Mat emFace = new Mat();
                Imgproc.cvtColor(face, emFace, Imgproc.COLOR_RGB2GRAY);

                Mat emBlob = Dnn.blobFromImage(emFace, 0.00392, new Size(48, 48), new Scalar(0, 0, 0), false, false);

                emotionModel.setInput(emBlob);
                Mat emMat = emotionModel.forward();

                Core.MinMaxLocResult emScores = Core.minMaxLoc(emMat);
                float emGuy = (float) emScores.maxVal;
                int emConf = (int) (emGuy * 100);
                Point emPoint = emScores.maxLoc;
                int emotion = (int) emPoint.x;

                // self._selection = ['Angry', 'Disgust', 'Fear', 'Happy', 'Sad', 'Surprise', 'Neutral']
                List<String> emotions = Arrays.asList("'Angry'", "Disgust", "Fear", "Happy", "Sad", "Surprise", "Neutral");
                String emotionText = emotions.get(emotion);

                //int confGuy = (int)(round(confs.get(idx),2)*100);
                String text = "Emo:" + emotionText + " " + emConf + "%|Gen:" + genderText + " " + genderConf + "%|Age:" + ageText + " " + ageConf + "%";


                Imgproc.putText(frame, text, new Point(left, top), Core.FONT_HERSHEY_SIMPLEX, .62, new Scalar(30, 80, 255), 2);
                Sounding.playFace(xCenter * 100 / frame.cols(), gender, age, emotion);
                MainActivity.SC_fun = -1;
                MainActivity.timer = 50;




            }
        }

        Log.d("EXPERIMENT8", "ACTUALLY WENT OUTSIDE THE LOOP");

    }

    private static void QOD_NET(Mat frame){

        Imgproc.cvtColor(frame, frame, Imgproc.COLOR_RGBA2RGB);
        //        //Creating a blob
        //['yolo_82', 'yolo_94', 'yolo_106']
        Mat blob = Dnn.blobFromImage(frame, 0.00392, new Size(416, 416), new Scalar(0, 0, 0),/*swapRB*/false, /*crop*/false);

        tinyYolo.setInput(blob);

        java.util.List<Mat> result = new java.util.ArrayList<Mat>(2);

        List<java.lang.String> outBlobNames = new java.util.ArrayList<>();
        outBlobNames.add(0, "yolo_16");
        outBlobNames.add(1, "yolo_23");
        //outBlobNames.add(2, "yolo_106");

        tinyYolo.forward(result, outBlobNames);

        //Toast.makeText(getApplicationContext(),"COMPUTED, BABY!" ,Toast.LENGTH_LONG).show();
        //Log.d("YOLO", "COLSSSS!!!! ---->>> "+Integer.toString(result.size())+" ");
        //Log.d("YOLO", "MATRIX! "+frame.toString());

        float confThreshold = 0.3f;
        List<Integer> clsIds = new ArrayList<>();
        List<Float> confs = new ArrayList<>();
        List<Rect> rects = new ArrayList<>();
        for (int i = 0; i < result.size(); ++i) {

            Mat level = result.get(i);
            for (int j = 0; j < level.rows(); ++j) {
                Mat row = level.row(j);
                Mat scores = row.colRange(5, level.cols());
                Core.MinMaxLocResult mm = Core.minMaxLoc(scores);

                float confidence = (float) mm.maxVal;


                Point classIdPoint = mm.maxLoc;


                if (confidence > confThreshold) {
                    int centerX = (int) (row.get(0, 0)[0] * frame.cols());
                    int centerY = (int) (row.get(0, 1)[0] * frame.rows());
                    int width = (int) (row.get(0, 2)[0] * frame.cols());
                    int height = (int) (row.get(0, 3)[0] * frame.rows());
                    int left = centerX - width / 2;
                    int top = centerY - height / 2;

                    clsIds.add((int) classIdPoint.x);
                    confs.add((float) confidence);
                    rects.add(new Rect(left, top, width, height));
                }
            }
        }
        int ArrayLength = confs.size();

        if (ArrayLength >= 1) {
            // Apply non-maximum suppression procedure.
            float nmsThresh = 0.2f;
            ;
            MatOfFloat confidences = new MatOfFloat(Converters.vector_float_to_Mat(confs));


            Rect[] boxesArray = rects.toArray(new Rect[0]);

            MatOfRect boxes = new MatOfRect(boxesArray);

            MatOfInt indices = new MatOfInt();


            Dnn.NMSBoxes(boxes, confidences, confThreshold, nmsThresh, indices);

            Sounding.init();
            //
            // Draw result boxes:
            int[] ind = indices.toArray();
            for (int i = 0; i < ind.length; ++i) {
                int idx = ind[i];
                Rect box = boxesArray[idx];
                int idGuy = clsIds.get(idx);
                int x = (int) ((box.x + box.width / 2) / (float) (frame.width()) * 100);
                //Imgproc.putText(frame,idGuy+"",box.tl(),Core.FONT_HERSHEY_SIMPLEX, 2, new Scalar(255,255,255),2);
                Imgproc.rectangle(frame, box.tl(), box.br(), new Scalar(0, 0, 255), 2);
                System.out.println(box);
                Sounding.AddItem(idGuy, x);
            }
            //Sounding.clearObjectStream();

            IsNetPrepared = true;
            if (!SpeechGenerator.isPlaying()) Sounding.PlayObjAssets();

        }else MainActivity.vibrator.vibrate(30);

    }

    private static void Bus_NET(Mat frame) {

        Imgproc.cvtColor(frame, frame, Imgproc.COLOR_RGBA2RGB);
        Mat blob = Dnn.blobFromImage(frame, 0.00392, new Size(416, 416), new Scalar(0, 0, 0),/*swapRB*/false, /*crop*/false);

        busYolo.setInput(blob);

        java.util.List<Mat> result = new java.util.ArrayList<Mat>(2);

        List<java.lang.String> outBlobNames = new java.util.ArrayList<>();
        outBlobNames.add(0, "yolo_16");
        outBlobNames.add(1, "yolo_23");

        busYolo.forward(result, outBlobNames);


        float confThreshold = 0.15f;
        List<Integer> clsIds = new ArrayList<>();
        List<Float> confs = new ArrayList<>();
        List<Rect> rects = new ArrayList<>();
        for (int i = 0; i < result.size(); ++i) {

            Mat level = result.get(i);
            for (int j = 0; j < level.rows(); ++j) {
                Mat row = level.row(j);
                Mat scores = row.colRange(5, level.cols());
                Core.MinMaxLocResult mm = Core.minMaxLoc(scores);

                float confidence = (float) mm.maxVal;


                Point classIdPoint = mm.maxLoc;


                if (confidence > confThreshold) {
                    int centerX = (int) (row.get(0, 0)[0] * frame.cols());
                    int centerY = (int) (row.get(0, 1)[0] * frame.rows());
                    int width = (int) (row.get(0, 2)[0] * frame.cols());
                    int height = (int) (row.get(0, 3)[0] * frame.rows());
                    int left = centerX - width / 2;
                    int top = centerY - height / 2;

                    clsIds.add((int) classIdPoint.x);
                    confs.add((float) confidence);
                    rects.add(new Rect(left, top, width, height));
                }
            }
        }
        int ArrayLength = confs.size();

        if (ArrayLength >= 1) {
            // Apply non-maximum suppression procedure.
            float nmsThresh = 0.2f;
            ;
            MatOfFloat confidences = new MatOfFloat(Converters.vector_float_to_Mat(confs));


            Rect[] boxesArray = rects.toArray(new Rect[0]);

            MatOfRect boxes = new MatOfRect(boxesArray);

            MatOfInt indices = new MatOfInt();


            Dnn.NMSBoxes(boxes, confidences, confThreshold, nmsThresh, indices);


            // Draw result boxes:
            int[] ind = indices.toArray();
            for (int i = 0; i < ind.length; ++i) {
                int idx = ind[i];
                Rect box = boxesArray[idx];
                int idGuy = clsIds.get(idx);
                int confGuy = (int) (confs.get(idx) * 100);

                if (idGuy == 3) {


                    int horGuy = (int) (((box.x + box.width) - box.x) * 0.25);
                    int verGuy = (int) (((box.y + box.height) - box.y) * 0.25);

                    int top = box.y - verGuy;
                    int left = box.x - horGuy;
                    int right = box.x + box.width + horGuy;
                    int bottom = box.y + box.height + verGuy;

                    Rect rectCropNum = new Rect(new Point(left, top), new Point(right, bottom));

                    Mat numROI = new Mat(frame, rectCropNum);


                    Mat imageBlobNums = Dnn.blobFromImage(numROI, 0.00392, new Size(54, 54), new Scalar(0, 0, 0), true, false);

                    java.util.List<Mat> resultNums = new java.util.ArrayList<Mat>(5);

                    List<java.lang.String> outBlobNamesNums = new java.util.ArrayList<>();
                    outBlobNamesNums.add(0, "dLength/Softmax");
                    outBlobNamesNums.add(1, "d1/Softmax");
                    outBlobNamesNums.add(2, "d2/Softmax");
                    outBlobNamesNums.add(3, "d3/Softmax");
                    outBlobNamesNums.add(4, "d4/Softmax");

                    busNum.setInput(imageBlobNums);
                    busNum.forward(resultNums, outBlobNamesNums);

//            Core.MinMaxLocResult genScores = Core.minMaxLoc(genderMat);
//            float genConf = (float)genScores.maxVal;
//            Point genId = genScores.maxLoc;


                    Core.MinMaxLocResult lengthScores = Core.minMaxLoc(resultNums.get(0));
                    Point lengthPoint = lengthScores.maxLoc;
                    int length = (int) lengthPoint.x;

                    String realNums = "";

                    for (int j = 1; j <= length + 1; ++j) {

                        Core.MinMaxLocResult numScores = Core.minMaxLoc(resultNums.get(j));
                        Point numPoint = numScores.maxLoc;
                        int num = (int) numPoint.x;

                        if (num != 10) {

                            realNums = realNums + num;

                        }

                    }


                    Imgproc.putText(frame, realNums, new Point(left, top), Core.FONT_HERSHEY_SIMPLEX, 1.75, new Scalar(255, 100, 100), 3);
                    Imgproc.rectangle(frame, new Point(left, top), new Point(right, bottom), new Scalar(255, 255, 0), 2);

                    BusSounding.addNum(box, realNums);

                } else {

                    Imgproc.putText(frame, idGuy + "|" + confGuy + "%", box.tl(), Core.FONT_HERSHEY_SIMPLEX, 1, new Scalar(255, 0, 100), 2);
                    Imgproc.rectangle(frame, box.tl(), box.br(), new Scalar(50, 25, 255), 2);

                    BusSounding.addElement(box, idGuy);

                    //BusSounding.addElement();

                }
            }
            IsNetPrepared = true;
            if (!SpeechGenerator.isPlaying()) BusSounding.playBuses();


        }

        MainActivity.SC_fun = -1;
        MainActivity.timer = 60;


    }


    public static Mat AOD_fun(Mat frame) {



        Sounding.WaitWorking();
        IsNetWorking = true;

        if(net == null){

            clearScs();
            AOD_load();

        }

        MainActivity.Window = MainActivity.WORKING_WINDOW;

        frame = AOD_NET(frame);

        IsNetWorking = false;

        MainActivity.SC_fun = -1;
        MainActivity.Window = MainActivity.MAIN_WINDOW;
        Sounding.WaitWorking();

        return frame;

    }

    public static Mat FD_fun(Mat frame) {

        IsNetWorking = true;

        if(faceModel == null){

            clearScs();
            FD_load();

        }

        FD_NET(frame);


        IsNetWorking = false;
        return frame;

    }

    public static Mat QOD_fun(Mat frame) {

        IsNetWorking = true;

        if(tinyYolo == null){

            clearScs();
            QOD_load();

        }

        QOD_NET(frame);

        IsNetWorking = false;


        return frame;
    }

    public static Mat Bus_fun(Mat frame) {

        IsNetWorking = true;

        if(busYolo == null){

            clearScs();
            Bus_load();

        }

        Bus_NET(frame);

        IsNetWorking = false;

        return frame;
    }


    public static void Bus_load(){
        String cfgPathBuses = Sounding.AssetLink.getFilesDir() + "/busyolo.cfg";
        String weightsPathBuses = Sounding.AssetLink.getFilesDir() + "/busyolo.weights";

//
//            String cfgPathBuses = getPath("busyolo.cfg", this);
//            String weightsPathBuses = getPath("busyolo.weights", this);

        busYolo = Dnn.readNetFromDarknet(cfgPathBuses, weightsPathBuses);

        String numsPath = Sounding.AssetLink.getFilesDir() + "/BUSNUM.pb";
        busNum = Dnn.readNetFromTensorflow(numsPath);
    }

    public static void AOD_load() {

        String cfgPath = Sounding.AssetLink.getFilesDir() + "/yolov3.cfg";
        String weightsPath = Sounding.AssetLink.getFilesDir() + "/yolov3.weights";
        net = Dnn.readNetFromDarknet(cfgPath, weightsPath);
        //Toast.makeText(Sounding.AssetLink,"Loaded big Yolo",Toast.LENGTH_LONG).show();

    }

    public static void FD_load() {

        String protoPath = Sounding.AssetLink.getFilesDir() + "/deploy.prototxt";
        String modelPath = Sounding.AssetLink.getFilesDir() + "/res10_300x300_ssd_iter_140000.caffemodel";
        detector = Dnn.readNetFromCaffe(protoPath, modelPath);

        String faceModelPath = Sounding.AssetLink.getFilesDir() + "/facemodel.pb";
        faceModel = Dnn.readNetFromTensorflow(faceModelPath);

        String emotionPath = Sounding.AssetLink.getFilesDir() + "/new_emotion_net.pb";
        emotionModel = Dnn.readNetFromTensorflow(emotionPath);

    }

    public static void QOD_load() {
        String cfgPathTiny = Sounding.AssetLink.getFilesDir() + "/yolov3-tiny.cfg";
        String weightsPathTiny = Sounding.AssetLink.getFilesDir() + "/yolov3-tiny.weights";
        tinyYolo = Dnn.readNetFromDarknet(cfgPathTiny, weightsPathTiny);
        //Toast.makeText(Sounding.AssetLink,"Loaded little Yolo",Toast.LENGTH_LONG).show();

    }


    private static Net clearSc(Net net){

        if(net != null){

            net = null;

        }
        return net;

    }

    private static void clearScs(){

        net = clearSc(net);
        tinyYolo = clearSc(tinyYolo);
        emotionModel = clearSc(emotionModel);
        detector = clearSc(detector);
        faceModel = clearSc(faceModel);
        busYolo = clearSc(busYolo);
        busNum = clearSc(busNum);

    }

}
