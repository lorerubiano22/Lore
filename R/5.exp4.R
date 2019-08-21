# Experiment 4: different policies
library("stringr")
library("MASS")
setwd("/home/laura/workspace/Lore/outputs/Exp4/")
# setwd("C://Users//usuario//Desktop//eclipse//workspace//Lore0818//outputs//Exp4//")
source("/home/laura/workspace/Lore/R/0.2reading.R")
# source("C://Users//usuario//Desktop//eclipse//workspace//Lore0818//R//0.2reading.R")


#mat1 <- mat[,c(1,10,11,43,51,52)]
mat1 <- mat[,c(1,10,11,44,52,53)]
for(j in c(4,5)){
  mat1[,j] <- as.numeric(as.character(mat1[,j]))}
sum(mat1[,6]=="false")
mat1 <- mat1[,-c(6)]
mat1 <- cbind(mat1, id = 1:dim(mat1)[1])

ids <- NULL
for(i in 1:length(levels(mat1$Instance))){
  aux <- subset(mat1, mat[,1]==levels(mat1$Instance)[i] & mat1[,2]=="false" & mat1[,3]=="false")
  ids <- c(ids, aux$id[which.min(aux$`BSS-exp total c`)])
  aux <- subset(mat1, mat[,1]==levels(mat1$Instance)[i] & mat1[,2]=="false" & mat1[,3]=="true")
  ids <- c(ids, aux$id[which.min(aux$`BSS-exp total c`)])
  aux <- subset(mat1, mat[,1]==levels(mat1$Instance)[i] & mat1[,2]=="true" & mat1[,3]=="true")
  ids <- c(ids, aux$id[which.min(aux$`BSS-exp total c`)])}

par(mfrow=c(1,2))
auxplot<-matrix(mat1[ids,4],ncol=3,byrow=TRUE)
# parcoord(auxplot)
maxV<-max(auxplot)
minV<-min(auxplot)
plot(1:3,1:3,type="n",ylim=c(minV,maxV),xaxt="n",ylab="Exp total cost",xlab="",cex.axis=0.95)
axis(1,at=1:3,c("No policies","\nCorrective\npolicies","All policies"),tick=FALSE,cex.axis=0.95)
points(rep(1,6),auxplot[,1],col=1:6)
for(i in 1:6){
  lines(x=c(1,2),y=c(auxplot[i,c(1:2)]),col=i,lty=i)}
points(rep(2,6),auxplot[,2],col=1:6)
for(i in 1:6){
  lines(x=c(2,3),y=c(auxplot[i,c(2:3)]),col=i,lty=i)}
points(rep(3,6),auxplot[,3],col=1:6)
legend("topright",legend=levels(mat1$Instance),cex=0.85,col=1:6,lty=1:6)

auxplot<-matrix(mat1[ids,5],ncol=3,byrow=TRUE)
# parcoord(auxplot)
maxV<-max(auxplot)
minV<-min(auxplot)
plot(1:3,1:3,type="n",ylim=c(minV,maxV),xaxt="n",ylab="Reliability",cex.axis=0.95,xlab="")
axis(1,at=1:3,c("No policies","\nCorrective\npolicies","All policies"),tick=FALSE,cex.axis=0.95)
points(rep(1,6),auxplot[,1],col=1:6)
for(i in 1:6){
  lines(x=c(1,2),y=c(auxplot[i,c(1:2)]),col=i,lty=i)}
points(rep(2,6),auxplot[,2],col=1:6)
for(i in 1:6){
  lines(x=c(2,3),y=c(auxplot[i,c(2:3)]),col=i,lty=i)}
points(rep(3,6),auxplot[,3],col=1:6)

# compute gaps and make boxplot