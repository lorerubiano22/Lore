# Experiment 1
library("stringr")
source("C://Users//usuario//Desktop//eclipse//workspace//Lore0818//R//0.reading.R")
# setwd("/home/laura/workspace/Lore/Experiments/Exp1/")
setwd("C://Users//usuario//Desktop//eclipse//workspace//Lore0818//outputs//Exp1//")

mat1 <- mat[,c(1,4,7:9,15,18:20,31)]
for(j in seq(2,9)){
  mat1[,j] <- as.numeric(as.character(mat1[,j]))}
sum(mat1[,10]=="false")

mat1 <- mat1[,-c(10)]

group0 <- subset(mat1, mat1[,5]==0.33)
group0 <- cbind(id = 1:dim(group0)[1],group0)
seq0 <- NULL
for(j in 1:length(unique(group0$Instance))){
  aux <- subset(group0, group0$Instance == unique(group0$Instance)[j])
  ids <- aux$id[which.min(aux$'BDS-total c')]
  seq0 <- c(seq0, ids)}
group0 <- group0[seq0,]

group1 <- subset(mat1, mat1[,3]==1)
group1 <- cbind(id = 1:dim(group1)[1],group1)
seq1 <- NULL
for(j in 1:length(unique(group1$Instance))){
  aux <- subset(group1, group1$Instance == unique(group1$Instance)[j])
  ids <- aux$id[which.min(aux$'BDS-economic c')]
  seq1 <- c(seq1, ids)}
group1 <- group1[seq1,]

group2 <- subset(mat1, mat1[,4]==1)
group2 <- cbind(id = 1:dim(group2)[1],group2)
seq2 <- NULL
for(j in 1:length(unique(group2$Instance))){
  aux <- subset(group2, group2$Instance == unique(group2$Instance)[j])
  ids <- aux$id[which.min(aux$'BDS-co2')]
  seq2 <- c(seq2, ids)}
group2 <- group2[seq2,]

group3 <- subset(mat1, mat1[,5]==1)
group3 <- cbind(id = 1:dim(group3)[1],group3)
seq3 <- NULL
for(j in 1:length(unique(group3$Instance))){
  aux <- subset(group3, group3$Instance == unique(group3$Instance)[j])
  ids <- aux$id[which.min(aux$'BDS-social c')]
  seq3 <- c(seq3, ids)}
group3 <- group3[seq3,]

mat2 <- matrix(0, ncol = 11, nrow = length(unique(mat1$Instance)))
mat2 <- data.frame(mat2)
mat2[,c(1:5)] <- group0[,c(2,7:10)]
mat2[,c(6,7)] <- group1[,c(7,8)]
mat2[,c(8,9)] <- group2[,c(7,9)]
mat2[,c(10,11)] <- group3[,c(7,10)]

names(mat2) <- c("Instance", "TC-balanced sol", "Ec-balanced sol", "Co2-balanced sol", "Social-balanced cost",
                 "TC-economic sol", "Ec-economic sol", "TC-co2 sol", "Co2-co2 sol", "TC-social sol", "Social-social sol")

mat3 <-matrix(0, ncol = 8, nrow = length(group0$Instance))
mat3 <- data.frame(mat3)
mat3[, c(1:2)] <- mat2[, c(1:2)]
mat3[,2] <- round(mat3[,2],2)
mat3[,3] <- round((mat2[,6] -  mat2[,2])/mat2[,2]*100,2)
mat3[,4] <- round((mat2[,7] -  mat2[,3])/mat2[,3]*100,2)
mat3[,5] <- round((mat2[,8] -  mat2[,2])/mat2[,2]*100,2)
mat3[,6] <- round((mat2[,9] -  mat2[,4])/mat2[,4]*100,2)
mat3[,7] <- round((mat2[,10] -  mat2[,2])/mat2[,2]*100,2)
mat3[,8] <- round((mat2[,11] -  mat2[,5])/mat2[,5]*100,2)

mat2[,c(1,2,3,6,7)]

names(mat3) <- c("Instance", "TC-balanced sol", 
                 "TC-economic sol (Gap %)", "Ec-economic sol (Gap %)", 
                 "TC-co2 sol (Gap %)", "Co2-co2 sol (Gap %)", 
                 "TC-social sol (Gap %)", "Social-social sol (Gap %)")

# adjustment needed
# A-n45-k6: 455, 468
# B-n63-k10: 1419, 1404
# mat1[c(1419, 1404),]
# Instance  Seed W economic W environment W social BDS-total c BDS-economic c BDS-co2 c BDS-social c
# 1419 B-n63-k10 24700       0.34          0.33     0.33    1538.831       1504.303    0.9818      33.5458
# 1404 B-n63-k10 24300       1.00          0.00     0.00    1471.520       1435.554    1.0073      34.9584

mat4 <- mat3
j <- 0
for (i in 1:dim(mat3)[1]){
  if (mat4[i,3] < 0){ # economic sol better than  TC in TC
    j <- j + 1
    bestEc <- subset(group1, group1$Instance == mat3[i,1])
    mat2[i,c(2:5)] <- bestEc[c(7:10)]
    mat4[i,2] <- round(mat3[i,2],2)
    mat4[i,3] <- round((mat2[i,6] -  mat2[i,2])/mat2[i,2]*100,2)
    mat4[i,4] <- round((mat2[i,7] -  mat2[i,3])/mat2[i,3]*100,2)
    mat4[i,5] <- round((mat2[i,8] -  mat2[i,2])/mat2[i,2]*100,2)
    mat4[i,6] <- round((mat2[i,9] -  mat2[i,4])/mat2[i,4]*100,2)
    mat4[i,7] <- round((mat2[i,10] -  mat2[i,2])/mat2[i,2]*100,2)
    mat4[i,8] <- round((mat2[i,11] -  mat2[i,5])/mat2[i,5]*100,2)}
  if (mat4[i,4] > 0){ # economic sol worse than  TC in ec
    j <- j + 1
    bestTC <- subset(group0, group0$Instance == mat3[i,1])
    mat2[i,c(6:7)] <- bestTC[c(7:8)]
    mat4[i,3] <- round((mat2[i,6] -  mat2[i,2])/mat2[i,2]*100,2)
    mat4[i,4] <- round((mat2[i,7] -  mat2[i,3])/mat2[i,3]*100,2)}
}
j

write.table(mat4, "res_exp1.txt", quote = FALSE, row.names = FALSE)